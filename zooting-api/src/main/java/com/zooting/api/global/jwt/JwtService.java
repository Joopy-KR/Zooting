package com.zooting.api.global.jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Collection;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Log4j2
@Getter
@Component
public class JwtService {
    private final SecretKey secretKey;
    private final long validityInMilliseconds;

    public JwtService(
            @Value("${jwt.secretKey}") String secretKey,
            @Value("${jwt.expiration-time}") long validityInMilliseconds
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createAccessToken(String userEmail, Collection<String> privileges){
        Date date = new Date();
        Date expirationDate = new Date(date.getTime() + validityInMilliseconds);

        String issuer = "Zooting";
        return Jwts.builder()
                .issuer(issuer)
                .expiration(expirationDate)
                .subject(userEmail)
                .claim("Privilege", privileges)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
    public boolean verifyToken(String token){
        log.debug("토큰 검증 시작: " + token);
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .after(new Date());
    }

    public Authentication getAuthentication(String token){
        return new UsernamePasswordAuthenticationToken(
                getUserEmail(token),
                null,
                AuthorityUtils
                        .createAuthorityList(
                                getPrivileges(token).stream()
                                        .map(privilege -> "ROLE_" + privilege)
                                        .toList()
                        )
        );
    }

    public String getUserEmail(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Collection<String> getPrivileges(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("Privilege", Collection.class);
    }
}
