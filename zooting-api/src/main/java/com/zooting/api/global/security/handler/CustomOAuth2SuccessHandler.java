package com.zooting.api.global.security.handler;

import com.zooting.api.domain.member.entity.Privilege;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import com.zooting.api.global.jwt.JwtService;
import com.zooting.api.global.security.oauth2.CustomOAuth2User;
import com.zooting.api.global.security.userdetails.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final StringRedisTemplate redisTemplate;
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${client.redirect-url.success}")
    private String REDIRECT_URI_SUCCESS;

    @Value("${client.redirect-url.anonymous}")
    private String REDIRECT_URI_ANONYMOUS;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");

        log.info("OAuth2SuecessHandler에 유저 정보가 전달되었습니다: " + oAuth2User);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        log.info("유저에게 Access Token과 Refresh Token을 발급합니다.");

        String accessToken = jwtService.createAccessToken(userDetails);
        String refreshToken = jwtService.createRefreshToken(userDetails);

        //TODO
        // redis 저장 테스트
        redisSaveTest(email, refreshToken);

        UriComponentsBuilder uriComponentsBuilder;

        if(isAnonymousMember(userDetails)){
            uriComponentsBuilder = UriComponentsBuilder.fromUriString(REDIRECT_URI_ANONYMOUS)
                            .queryParam("access-token", accessToken);

        } else {
            uriComponentsBuilder = UriComponentsBuilder.fromUriString(REDIRECT_URI_SUCCESS)
                    .queryParam("access-token", accessToken);
        }

        //TODO
        // 테스트용 리프레시 토큰 쿠키
        // 개선 필요
        ResponseCookie cookie = ResponseCookie.from("refresh-token", refreshToken)
                .maxAge(30 * 24 * 60 * 60)
                .path("/")
                .secure(true)
                .sameSite("Lax") // Same site 설정 필요
                .domain("localhost")  //어느 도메인에 열어줄 것인가
                .build();

        response.setHeader("Set-cookie", cookie.toString());

        String redirectURI = uriComponentsBuilder.toUriString();
        response.sendRedirect(redirectURI);
    }
    public boolean isAnonymousMember(UserDetails userDetails){
        if (userDetails.getAuthorities().isEmpty()) {
            throw new BaseExceptionHandler(ErrorCode.UNAUTHORIZED_USER_EXCEPTION);
        }
        return userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Privilege.ANONYMOUS.name()));
    }

    public void redisSaveTest(String email, String refreshToken){
        log.info("리프레시 토큰 저장 테스트");
        redisTemplate.opsForValue().set(email, refreshToken, 15, TimeUnit.DAYS);
        log.info("리프레시 토큰 저장 완료");
    }
}