package com.zooting.api.global.jwt;

import com.google.gson.Gson;
import com.zooting.api.global.common.ErrorResponse;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
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
    private static final String ACCESS_HEADER_AUTHORIZATION = "access-token";
    private static final String TOKEN_PREFIX = "Bearer ";
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

        log.info("1. 유저 Request로부터 Access Token을 가져옵니다");
        String accessToken = tokenProcessor(request, ACCESS_HEADER_AUTHORIZATION);
        log.info("2. 유저 Request로부터 Access Token을 가져왔습니다: " + accessToken);

        try {
            log.info("3. Access Token 인증을 시작합니다.");
            Authentication authentication = jwtService.verifyAccessToken(accessToken);
            log.info("4. 인증이 성공적으로 완료되었습니다.");

            log.info("5. 유저의 Access Token 인증 정보를 SecurityContextHolder에 저장했습니다.");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.info("4. 유저의 Access Token이 만료되었습니다. 토큰 재발급이 필요합니다");
            throw new BaseExceptionHandler(ErrorCode.EXPIRED_ACCESS_TOKEN_EXCEPTION);
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException e) {
            log.info("4. 유효하지 않은 토큰입니다.");
            throw new BaseExceptionHandler(ErrorCode.INVALID_ACCESS_TOKEN_EXCEPTION);
        } catch (IllegalArgumentException e){
            Gson gson = new Gson();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            gson.toJson(ErrorResponse.of().code(ErrorCode.NOT_VALID_HEADER_ERROR).build(), response.getWriter());
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