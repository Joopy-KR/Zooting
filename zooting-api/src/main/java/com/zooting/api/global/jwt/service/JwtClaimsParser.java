package com.zooting.api.global.jwt.service;

import io.jsonwebtoken.Claims;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtClaimsParser {
    public static Collection<GrantedAuthority> getPrivileges(Claims claims) {
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
