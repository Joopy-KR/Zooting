package com.zooting.api.global.security.oauth2;

import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
    public CustomOAuth2User(Map<String, Object> attributes, String nameAttributeKey) {
        super(AuthorityUtils.NO_AUTHORITIES, attributes, nameAttributeKey);
    }
}
