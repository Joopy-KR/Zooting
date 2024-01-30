package com.zooting.api.global.jwt.service;

import com.zooting.api.global.security.userdetails.CustomUserDetails;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtCreator {
    private final String issuer;
    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    JwtCreator(
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.secretKey}") String secretKey,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.issuer = issuer;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public JwtBuilder createToken(CustomUserDetails userDetails, long expirationTime) {
        Date date = new Date();
        Date expirationDate = new Date(date.getTime() + expirationTime);

        return Jwts.builder()
                .signWith(secretKey, Jwts.SIG.HS256)
                .issuer(issuer)
                .expiration(expirationDate)
                .subject(userDetails.getUsername())
                .claim("nickname", userDetails.getNickname());
    }

    public String createAccessToken(CustomUserDetails userDetails) {
        return createToken(userDetails, accessTokenExpiration)
                .claim("Privilege",
                        userDetails
                                .getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList()
                )
                .compact();
    }
    public String createRefreshToken(CustomUserDetails userDetails) {
        return createToken(userDetails, refreshTokenExpiration).compact();
    }

    public ResponseCookie buildResponseCookie(String refreshToken) {
        return ResponseCookie.from("refresh-token", refreshToken)
                .maxAge(refreshTokenExpiration)
                .path("/")
                .secure(true)
                .sameSite("Lax") // Same site 설정 필요
                .domain("localhost")  //어느 도메인에 열어줄 것인가
                .build();
    }

}
