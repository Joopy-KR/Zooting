package com.zooting.api.global.security;

import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
    String email;
    public CustomOAuth2User(
            Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes,
            String nameAttributeKey, String email
    ) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
    }
}
