package com.zooting.api.global.jwt.controller;

import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.common.code.SuccessCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import com.zooting.api.global.jwt.dto.TokenDto;
import com.zooting.api.global.jwt.service.JwtService;
import com.zooting.api.global.security.userdetails.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class JwtController {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    @Operation(summary = "액세스 토큰 재발급 요청하기", description = "액세스 토큰 없거나 만료됐으면 재발급 요청하기")
    @PostMapping("/refresh")
    public ResponseEntity<?> sendRefreshRequest(@Valid @NotNull @CookieValue(value = "refresh-token") String refreshToken){
        String email = jwtService.verifyRefreshToken(refreshToken);
        log.info("리프레시 토큰의 유효성을 검증했습니다. Redis에 저장된 토큰과 비교합니다.");
        String refreshTokenInServer = jwtService.getRefreshTokenRedis(email);

        if(refreshTokenInServer.equals(refreshToken)) {
            log.info("리프레시 토큰과 Redis Reository에 저장된 토큰이 일치함을 확인했습니다");

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            String accessToken = jwtService.createAccessToken(userDetails);
            String newRefreshToken = jwtService.createRefreshToken(userDetails);
//            ResponseCookie responseCookie = jwtService.buildResponseCookie(newRefreshToken);
            jwtService.saveRefreshTokenRedis(email, newRefreshToken);

            return BaseResponse.success(
                    SuccessCode.CHECK_SUCCESS,
                    new TokenDto(accessToken, newRefreshToken)
            );
        } else {
            throw new BaseExceptionHandler(ErrorCode.INVALID_REFRESH_TOKEN_EXCEPTION);
        }
    }
}