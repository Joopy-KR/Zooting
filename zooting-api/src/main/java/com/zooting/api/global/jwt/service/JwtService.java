package com.zooting.api.global.jwt.service;

import com.zooting.api.global.jwt.dto.TokenDto;
import com.zooting.api.global.security.userdetails.CustomUserDetails;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class JwtService {
    private final JwtRepository jwtRepository;
    private final JwtVerifier jwtVerifier;
    private final JwtCreator jwtCreator;
    private final UserDetailsService userDetailsService;
    public JwtService(
            JwtVerifier jwtVerifier,
            JwtRepository jwtRepository,
            JwtCreator jwtCreator,
            UserDetailsService userDetailsService
    ) {
        this.jwtVerifier = jwtVerifier;
        this.jwtRepository = jwtRepository;
        this.jwtCreator = jwtCreator;
        this.userDetailsService = userDetailsService;
    }

    public TokenDto getJwtTokens(String refreshToken){
        TokenDto tokenDto = null;
        Claims claims = jwtVerifier.verifyJwtToken(refreshToken);

        String email = claims.getSubject();
        String refreshTokenInServer = jwtRepository.get(email);

        if(refreshTokenInServer.equals(refreshToken)){
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String newAccessToken = jwtCreator.createAccessToken(userDetails);
            String newRefreshToken = jwtCreator.createRefreshToken(userDetails);

            jwtRepository.save(email, newRefreshToken);

            tokenDto = new TokenDto(newAccessToken, newRefreshToken);

        }
        return tokenDto;
    }

    public Authentication authenticateToken(String token){
        Claims claims = jwtVerifier.verifyJwtToken(token);
        JwtClaimsParser jwtClaimsParser = new JwtClaimsParser(claims);

        UserDetails userDetails = CustomUserDetails.builder()
                .email(claims.getSubject())
                .authorities(jwtClaimsParser.getPrivileges())
                .build();

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }
}
