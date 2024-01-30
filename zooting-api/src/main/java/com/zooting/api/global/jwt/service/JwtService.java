package com.zooting.api.global.jwt.service;

import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import com.zooting.api.global.jwt.dto.TokenDto;
import com.zooting.api.global.security.userdetails.CustomUserDetails;
import com.zooting.api.global.security.userdetails.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
@Log4j2
@Service
public class JwtService {
    private final JwtRepository jwtRepository;
    private final JwtVerifier jwtVerifier;
    private final JwtCreator jwtCreator;
    private final CustomUserDetailsService userDetailsService;
    public JwtService(
            JwtVerifier jwtVerifier,
            JwtRepository jwtRepository,
            JwtCreator jwtCreator,
            CustomUserDetailsService userDetailsService
    ) {
        this.jwtVerifier = jwtVerifier;
        this.jwtRepository = jwtRepository;
        this.jwtCreator = jwtCreator;
        this.userDetailsService = userDetailsService;
    }

    public TokenDto rotateJwtTokens(String refreshToken){
        Claims claims = jwtVerifier.verifyJwtToken(refreshToken);
        String email = claims.getSubject();
        String refreshTokenInServer = jwtRepository.get(email);

        log.info("Refresh Token Rotation 요청이 들어왔습니다.");

        if(refreshTokenInServer.equals(refreshToken)) {
            log.info("요청의 Refresh Token이 Redis에 저장된 값과 일치합니다.");

            CustomUserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String newAccessToken = jwtCreator.createAccessToken(userDetails);
            String newRefreshToken = jwtCreator.createRefreshToken(userDetails);
            jwtRepository.save(email, newRefreshToken);

            return new TokenDto(newAccessToken, newRefreshToken);
        } else {
            throw new BaseExceptionHandler(ErrorCode.INCONSISTENT_REFRESH_TOKEN_EXCEPTION);
        }
    }

    public Authentication authenticateToken(String token){
        Claims claims = jwtVerifier.verifyJwtToken(token);
        JwtClaimsParser jwtClaimsParser = new JwtClaimsParser(claims);

        log.info("토큰의 Claims에 저장된 닉네임:" + claims.get("nickname"));

        UserDetails userDetails = CustomUserDetails.builder()
                .email(claims.getSubject())
                .nickname((String) claims.get("nickname"))
                .authorities(jwtClaimsParser.getPrivileges())
                .build();

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }
}