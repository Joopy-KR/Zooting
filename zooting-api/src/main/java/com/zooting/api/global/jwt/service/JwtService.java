package com.zooting.api.global.jwt.service;

import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import com.zooting.api.global.jwt.dao.JwtRedisDao;
import com.zooting.api.global.jwt.dto.TokenDto;
import com.zooting.api.global.security.userdetails.CustomUserDetails;
import com.zooting.api.global.security.userdetails.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class JwtService {

    private static final String ACCESS_HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final String issuer;
    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final JwtRedisDao jwtRedisDao;
    private final CustomUserDetailsService userDetailsService;

    public JwtService(JwtRedisDao jwtRedisDao, CustomUserDetailsService userDetailsService,
            @Value("${jwt.issuer}") String issuer, @Value("${jwt.secretKey}") String secretKey,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.jwtRedisDao = jwtRedisDao;
        this.issuer = issuer;
        this.userDetailsService = userDetailsService;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    /**
     * 로그인을 요청한 유저의 Request에서 토큰 정보를 가져오고, 권한을 부여한다.
     *
     * @param request 로그인한 유저의 Request
     * @return Access Token에 있던 유저 정보를 기반으로 한 인증 객체
     */
    public Authentication authenticateAccessToken(HttpServletRequest request) {
        String token = requestHeaderJwtParser(request);
        Claims claims = verifyJwtToken(token);

        log.trace("토큰의 Claims에 저장된 닉네임:" + claims.get("nickname"));

        UserDetails userDetails = CustomUserDetails.builder().email(claims.getSubject())
                .nickname((String) claims.get("nickname")).authorities(JwtClaimsParser.getPrivileges(claims)).build();

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    /**
     * 로그인을 요청한 유저의 Request Header에서 Access Token 정보를 가져온다.
     *
     * @param request 유저의 Request
     * @return Request Header에서 가져온  Access Token 정보
     */
    public String requestHeaderJwtParser(HttpServletRequest request) {
        String token = request.getHeader(ACCESS_HEADER_AUTHORIZATION);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(7);
        }
        return null;
    }

    public String createAccessToken(CustomUserDetails userDetails) {
        Date date = new Date();
        Date expirationDate = new Date(date.getTime() + accessTokenExpiration * 1000);

        return Jwts.builder().signWith(secretKey, Jwts.SIG.HS256).issuer(issuer).expiration(expirationDate)
                .subject(userDetails.getUsername()).claim("nickname", userDetails.getNickname())
                .claim("Privilege", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .compact();
    }

    public String createRefreshToken(CustomUserDetails userDetails) {
        Date date = new Date();
        Date expirationDate = new Date(date.getTime() + refreshTokenExpiration * 1000);

        String refreshToken = Jwts.builder().signWith(secretKey, Jwts.SIG.HS256).issuer(issuer)
                .expiration(expirationDate).subject(userDetails.getUsername())
                .claim("nickname", userDetails.getNickname()).compact();

        jwtRedisDao.save(userDetails.getEmail(), refreshToken, refreshTokenExpiration);
        return refreshToken;
    }

    public TokenDto rotateJwtTokens(String refreshToken) {
        Claims claims = verifyJwtToken(refreshToken);
        String email = claims.getSubject();
        String refreshTokenInServer = jwtRedisDao.get(email);

        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(email);

        log.trace("Refresh Token Rotation 요청이 들어왔습니다.");

        if (refreshTokenInServer.equals(refreshToken)) {
            log.trace("요청의 Refresh Token이 Redis에 저장된 값과 일치합니다.");
            String newAccessToken = createAccessToken(userDetails);
            String newRefreshToken = createRefreshToken(userDetails);
            jwtRedisDao.save(email, newRefreshToken, refreshTokenExpiration);

            return new TokenDto(newAccessToken, newRefreshToken);
        } else {
            throw new BaseExceptionHandler(ErrorCode.INCONSISTENT_REFRESH_TOKEN_EXCEPTION);
        }
    }

    public Claims verifyJwtToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token) // Throws JWT Exception
                .getPayload();
    }

    public ResponseCookie buildResponseCookie(String refreshToken) {
        return ResponseCookie.from("refresh-token", refreshToken).maxAge(refreshTokenExpiration).path("/").secure(true)
                .sameSite("Lax") // Same site 설정 필요
                .domain("localhost")  //어느 도메인에 열어줄 것인가
                .build();
    }
}