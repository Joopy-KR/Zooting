package com.zooting.api.global.jwt.service;

import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import com.zooting.api.global.security.userdetails.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Log4j2
@Getter
@Component
@RequiredArgsConstructor
public class JwtService {
    private final String issuer;
    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final StringRedisTemplate redisTemplate;


    @Autowired
    public JwtService(
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.secretKey}") String secretKey,
            @Value("${jwt.access-token-expiration") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration,
            StringRedisTemplate redisTemplate
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.issuer = issuer;
        this.redisTemplate = redisTemplate;
    }

    public JwtBuilder createToken(UserDetails userDetails, long expirationTime){
        Date date = new Date();
        Date expirationDate = new Date(date.getTime() + expirationTime);

        return Jwts.builder()
                .signWith(secretKey, Jwts.SIG.HS256)
                .issuer(issuer)
                .expiration(expirationDate)
                .subject(userDetails.getUsername());
    }

    public String createAccessToken(UserDetails userDetails){
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
    public String createRefreshToken(UserDetails userDetails){
        return createToken(userDetails, refreshTokenExpiration).compact();
    }

    public Authentication verifyAccessToken(String token){
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token) // Throws JWT Exception
                    .getPayload();

            UserDetails userDetails = CustomUserDetails.builder()
                    .email(claims.getSubject())
                    .authorities(getPrivileges(claims))
                    .build();

            return new UsernamePasswordAuthenticationToken(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities());
    }

    public String verifyRefreshToken(String token){
        try{
            log.info("Refresh Token 검증을 시작합니다");
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token) // Throws JWT Exception
                    .getPayload();

            String email = claims.getSubject();

            log.info("토큰이 정상적으로 검증되었습니다");
            log.info("Refresh Token 사용자: " + email);

            return email;
        } catch (ExpiredJwtException e) {
            log.info("Refresh Token이 만료되었습니다. 로그인 페이지로 이동합니다.");
            // TODO: Redirect to login Page
            throw new BaseExceptionHandler(ErrorCode.EXPIRED_REFRESH_TOKEN_EXCEPTION);
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException e) {
            log.info("유효하지 않은 토큰입니다.");
            throw new BaseExceptionHandler(ErrorCode.INVALID_REFRESH_TOKEN_EXCEPTION);
        }
    }

//    else { // Access 토큰이 없음 -> 리프레쉬 토큰이 있는지 확인후 재발급
//        log.info("Access Token이 없습니다. Refresh Token 보유 여부를 확인합니다");
//        String refreshToken = null;
//
//        Cookie[] cookies = request.getCookies();
//
//        for(Cookie cookie : cookies){
//            if(cookie.getName().equals(REFRESH_HEADER_AUTHORIZATION)){
//                refreshToken = cookie.getValue();
//                break;
//            }
//        }
//
//        if(refreshToken != null){
//            String email = jwtService.verifyRefreshToken(refreshToken);
//
//            log.info("리프레시 토큰의 유효성을 검증했습니다. 서버에 저장된 토큰과 비교합니다.");
//            String refreshTokenInServer = redisTemplate.opsForValue().get(email);
//
//            if(!refreshToken.equals(refreshTokenInServer)){
//                log.info("유저의 리프레시 토큰이 서버의 토큰과 일치하지 않습니다");
//            } else {
//                log.info("리프레시 토큰을 확인했습니다");
//                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
//
//                accessToken = jwtService.createAccessToken(userDetails);
//
//                UriComponentsBuilder uriComponentsBuilder;
//                uriComponentsBuilder = UriComponentsBuilder.fromUriString(REDIRECT_URI_SUCCESS)
//                        .queryParam("access-token", accessToken);
//
//                String redirectURI = uriComponentsBuilder.toUriString();
//                response.sendRedirect(redirectURI);
//            }
//        } else { // 토큰이 둘 다 없는 사용자
//            log.info("토큰이 없는 사용자입니다.");
//            throw new BaseExceptionHandler(ErrorCode.UNAUTHORIZED_USER_EXCEPTION);
//        }
//    }

    public void saveRefreshTokenRedis(String email, String refreshToken){
        redisTemplate.opsForValue().set(email, refreshToken, 15, TimeUnit.DAYS);
    }

    public Collection<GrantedAuthority> getPrivileges(Claims claims){
        Object stringAuthorities = claims.get("Privilege");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (stringAuthorities instanceof Collection<?>){
            for(Object grantedAuthority : (Collection<?>) stringAuthorities){
                if(grantedAuthority instanceof String){
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + grantedAuthority));
                }
            }
        }
        return authorities;
    }
}
