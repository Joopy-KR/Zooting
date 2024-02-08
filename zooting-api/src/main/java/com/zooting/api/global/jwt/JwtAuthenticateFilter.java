package com.zooting.api.global.jwt;

import com.google.gson.Gson;
import com.zooting.api.global.common.ErrorResponse;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.jwt.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticateFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final String[] URL_WHITE_LIST;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.info("Request URI: {}", request.getRequestURI());
        log.info("Request Method: {}", request.getMethod());
        log.info("Request Params: {}", request.getParameterMap());
        log.info("Access-token: {}", request.getHeader("Authorization"));
        if (PatternMatchUtils.simpleMatch(URL_WHITE_LIST, request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            log.info("유저의 토큰을 검증합니다.");
            Authentication authentication = jwtService.authenticateAccessToken(request);

            log.info("유저의 토큰이 검증되었습니다. 유저를 SecurityContextHolder에 저장합니다.");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.info("유저의 액세스 토큰이 만료되었습니다.");
            sendJwtErrorResponse(ErrorCode.EXPIRED_ACCESS_TOKEN_EXCEPTION, response);
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException e) {
            log.info("유저의 액세스 토큰이 타당하지 않습니다.");
            sendJwtErrorResponse(ErrorCode.INVALID_ACCESS_TOKEN_EXCEPTION, response);
        } catch (IllegalArgumentException e) {
            log.info("유저의 액세스 토큰이 존재하지 않습니다.");
            sendJwtErrorResponse(ErrorCode.ILLEGAL_TOKEN_EXCEPTION, response);
        }
    }

    public void sendJwtErrorResponse(ErrorCode errorCode, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getStatus());
        gson.toJson(ErrorResponse.of().code(errorCode).build(), response.getWriter());
    }
}