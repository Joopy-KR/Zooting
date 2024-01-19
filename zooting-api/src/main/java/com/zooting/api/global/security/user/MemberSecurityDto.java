package com.zooting.api.global.security.user;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class MemberSecurityDto extends User {
    public MemberSecurityDto(String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
}
