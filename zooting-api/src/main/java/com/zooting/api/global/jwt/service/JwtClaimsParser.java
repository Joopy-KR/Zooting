package com.zooting.api.global.jwt.service;

import io.jsonwebtoken.Claims;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@Setter
public class JwtClaimsParser {
    private final Claims claims;
    JwtClaimsParser(Claims claims){
        this.claims = claims;
    }

    public Collection<GrantedAuthority> getPrivileges() {
        Object stringAuthorities = claims.get("Privilege");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (stringAuthorities instanceof Collection<?>) {
            for (Object grantedAuthority : (Collection<?>) stringAuthorities) {
                if (grantedAuthority instanceof String) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + grantedAuthority));
                }
            }
        }
        return authorities;
    }
}
