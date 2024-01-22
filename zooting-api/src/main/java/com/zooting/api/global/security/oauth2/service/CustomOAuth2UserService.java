package com.zooting.api.global.security.oauth2.service;

import com.zooting.api.global.security.oauth2.CustomOAuth2User;
import com.zooting.api.global.security.oauth2.OAuth2Parser;
import com.zooting.api.global.security.oauth2.OAuth2Provider;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final OAuth2Parser oAuth2Parser;
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String userId = oAuth2User.getName();

        OAuth2Provider provider = oAuth2Parser.getProvider(oAuth2UserRequest);
        String email = oAuth2Parser.getEmail(attributes, provider);
        String userNameAttributeName = oAuth2Parser.getNameAttributeName(oAuth2UserRequest);

        Map<String, Object> customAttributes = new HashMap<>();

        customAttributes.put(userNameAttributeName, userId);
        customAttributes.put("Provider", provider);
        customAttributes.put("email", email);

        return new CustomOAuth2User(
                customAttributes,
                userNameAttributeName
        );
    }
}