package com.zooting.api.global.security.handler;

import com.zooting.api.domain.member.entity.Privilege;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import com.zooting.api.global.jwt.service.JwtCreator;
import com.zooting.api.global.jwt.service.JwtRepository;
import com.zooting.api.global.security.userdetails.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtCreator jwtCreator;
    private final JwtRepository jwtRepository;

    private final CustomUserDetailsService customUserDetailsService;

    @Value("${client.redirect-url.success}")
    private String REDIRECT_URI_SUCCESS;

    @Value("${client.redirect-url.anonymous}")
    private String REDIRECT_URI_ANONYMOUS;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");

        log.info("5. 유저 정보가 성공적으로 전달되었습니다: " + oAuth2User);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        log.info("6. 유저에게 Access Token과 Refresh Token을 발급합니다.");

        String accessToken = jwtCreator.createAccessToken(userDetails);
        log.info("7. 유저에게 Access Token 발급을 완료했습니다.");

        String refreshToken = jwtCreator.createRefreshToken(userDetails);
        log.info("8. 유저에게 Refresh Token 발급을 완료했습니다.");

        log.info("9. 리프레시 토큰을 Redis Repository에 저장합니다.");
        jwtRepository.save(email, refreshToken);
        log.info("10. 리프레시 토큰을 Redis Repository에 저장했습니다.");

        UriComponentsBuilder uriComponentsBuilder;

        log.info("11. 유저의 추가 정보 기입 여부를 확인합니다");
        if (isAnonymousMember(userDetails)) {
            log.info("12. 추가 기입 정보를 입력하지 않은 유저입니다. 추가 정보 기입 페이지로 URL을 설정합니다.");
            uriComponentsBuilder = UriComponentsBuilder.fromUriString(REDIRECT_URI_ANONYMOUS)
                    .queryParam("access-token", accessToken)
                    .queryParam("refresh-token", refreshToken);

        } else {
            log.info("12. 추가 기입 정보를 입력한 유저입니다. 로그인 완료 페이지로 URL을 설정합니다.");
            uriComponentsBuilder = UriComponentsBuilder.fromUriString(REDIRECT_URI_SUCCESS)
                    .queryParam("access-token", accessToken)
                    .queryParam("refresh-token", refreshToken);
        }

        log.info("12.5 : 리프레시 토큰 정보" + refreshToken);
        log.info("13. 응답 헤더에 Refresh Token을 Http Only Cookie로 저장했습니다");

        response.setHeader("Set-cookie", jwtCreator.buildResponseCookie(refreshToken).toString());
        String redirectURI = uriComponentsBuilder.toUriString();
        response.sendRedirect(redirectURI);
    }

    public boolean isAnonymousMember(UserDetails userDetails) {
        if (userDetails.getAuthorities().isEmpty()) {
            throw new BaseExceptionHandler(ErrorCode.UNAUTHORIZED_USER_EXCEPTION);
        }
        return userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Privilege.ANONYMOUS.name()));
    }
}