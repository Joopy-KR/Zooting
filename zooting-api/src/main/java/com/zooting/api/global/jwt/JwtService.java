package com.zooting.api.global.jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtService {
    private final SecretKey secretKey;
    private final long validityInMilliseconds;

    public JwtService(
            @Value("${jwt.secretKey}") String secretKey,
            @Value("${jwt.expiration-time}") long validityInMilliseconds
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createAccessToken(String userEmail){
        Date date = new Date();
        Date expirationDate = new Date(date.getTime() + validityInMilliseconds);

        String issuer = "Zooting";
        return Jwts.builder()
                .claim("userEmail", userEmail)
                .signWith(secretKey, Jwts.SIG.HS256)
                .issuer(issuer)
                .expiration(expirationDate)
                .subject(userEmail)
                .compact();
    }

    public boolean verifyToken(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }
}
