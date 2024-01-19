package com.zooting.api.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.web.filter.OncePerRequestFilter;

@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticateFilter extends OncePerRequestFilter {
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {


        if (!request.getRequestURI().startsWith("api")) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("필터 검증 진입");
        String token = tokenProcessor(request);

        // Test
        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();


        if (token != null && jwtService.verifyToken(token)) {
            Authentication authentication = jwtService.getAuthentication(token);
           log.info("Authentication 정보" + authentication);

           log.info("SecurityContextHolder 정보 Before : "+ securityContextHolderStrategy.getContext());
           SecurityContextHolder.getContext().setAuthentication(authentication);
           log.info("SecurityContextHolder 정보 After : "+ securityContextHolderStrategy.getContext());

            log.info("Security Context에 " + authentication.getName() + "의 정보를 저장했다.");
            filterChain.doFilter(request, response);
        } else {
            log.debug("요청에 유효한 토큰이 없다.");
        }
    }

    public String tokenProcessor(HttpServletRequest request) {
        String token = request.getHeader(HEADER_AUTHORIZATION);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(7);
        }
        log.info("요청에 토큰 정보가 없습니다");
        return null;
    }
}