package com.zooting.api.global.jwt;

import com.zooting.api.global.security.userdetails.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticateFilter extends OncePerRequestFilter {
    private static final String ACCESS_HEADER_AUTHORIZATION = "Authorization";
    private static final String REFRESH_HEADER_AUTHORIZATION = "refresh-token";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Value("${client.redirect-url.success}")
    private String REDIRECT_URI_SUCCESS;

    private final StringRedisTemplate redisTemplate;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;
    private final String[] URL_WHITE_LIST;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (PatternMatchUtils.simpleMatch(URL_WHITE_LIST, request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("유저 Request로부터 Access Token을 가져옵니다");
        String accessToken = tokenProcessor(request, ACCESS_HEADER_AUTHORIZATION);
        log.info("유저 Request로부터 Access Token을 가져왔습니다: " + accessToken);

        if(accessToken != null) {
            log.info("검증된 토큰으로부터 인증 객체를 생성합니다.");
            Authentication authentication = jwtService.verifyToken(accessToken);
            log.info(authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else { // Access 토큰이 없음 -> 리프레쉬 토큰이 있는지 확인후 재발급
            log.info("Access Token이 없습니다. Refresh Token 보유 여부를 확인합니다");
            String refreshToken = null;

            Cookie[] cookies = request.getCookies();

            for(Cookie cookie : cookies){
                if(cookie.getName().equals(REFRESH_HEADER_AUTHORIZATION)){
                   refreshToken = cookie.getValue();
                   break;
                }
            }

            if(refreshToken != null){
                String email = jwtService.verifyRefreshToken(refreshToken);

                log.info("리프레시 토큰의 유효성을 검증했습니다. 서버에 저장된 토큰과 비교합니다.");
                String refreshTokenInServer = redisTemplate.opsForValue().get(email);

                if(!refreshToken.equals(refreshTokenInServer)){
                    log.info("유저의 리프레시 토큰이 서버의 토큰과 일치하지 않습니다");
                } else {
                    log.info("리프레시 토큰을 확인했습니다");
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                    accessToken = jwtService.createAccessToken(userDetails);

                    UriComponentsBuilder uriComponentsBuilder;
                    uriComponentsBuilder = UriComponentsBuilder.fromUriString(REDIRECT_URI_SUCCESS)
                            .queryParam("access-token", accessToken);

                    String redirectURI = uriComponentsBuilder.toUriString();
                    response.sendRedirect(redirectURI);
                }
            } else { // 토큰이 둘 다 없는 사용자
                log.info("토큰이 없는 사용자입니다.");
            }

        }
    }

    public String tokenProcessor(HttpServletRequest request, String header) {
        String token = request.getHeader(header);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(7);
        }
        return null;
    }
}