package com.zooting.api.global.jwt;

import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import com.zooting.api.global.security.userdetails.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Log4j2
@Getter
@Component
public class JwtService {
    private final SecretKey secretKey;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;


    public JwtService(
            @Value("${jwt.secretKey}") String secretKey,
            @Value("${jwt.access-token-expiration}") long accessTokenExpirationTime,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpirationTime
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this. refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    public String createAccessToken(UserDetails userDetails){
        return createToken(userDetails, accessTokenExpirationTime);
    }
    public String createRefreshToken(UserDetails userDetails){
        return createToken(userDetails, refreshTokenExpirationTime);
    }

    public String createToken(UserDetails userDetails, long expirationTime){
        Date date = new Date();
        Date expirationDate = new Date(date.getTime() + expirationTime);

        String issuer = "Zooting";
        return Jwts.builder()
                .issuer(issuer)
                .expiration(expirationDate)
                .subject(userDetails.getUsername())
                .claim("Privilege",
                        userDetails.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority).toList())
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public Authentication verifyToken(String token){
        try{
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token) // Throws JWT Exception
                    .getPayload();

            UserDetails userDetails = CustomUserDetails.builder()
                    .email(claims.getSubject())
                    .authorities(getPrivileges(claims))
                    .build();

            log.info(userDetails);

            return new UsernamePasswordAuthenticationToken(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities());

        } catch (ExpiredJwtException e) {
            throw new BaseExceptionHandler(ErrorCode.EXPIRED_ACCESS_TOKEN_EXCEPTION);
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException e) {
            throw new BaseExceptionHandler(ErrorCode.INVALID_ACCESS_TOKEN_EXCEPTION);
        }
    }

    public Collection<GrantedAuthority> getPrivileges(Claims claims){
        Object stringAuthorities = claims.get("Privilege");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (stringAuthorities instanceof Collection<?>){
            for(Object grantedAuthority : (Collection<?>) stringAuthorities){
                if(grantedAuthority instanceof String){
                    authorities.add(new SimpleGrantedAuthority((String) grantedAuthority));
                }
            }
        }
        return authorities;
    }
}
