package com.zooting.api.global.security.oauth2.service;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public abstract class OAuth2RequestProcessor extends DefaultOAuth2UserService {
    private final OAuth2UserRequest oAuth2UserRequest;
    private final OAuth2User oAuth2User;
    private final String registrationId;
    OAuth2RequestProcessor(OAuth2UserRequest oAuth2UserRequest){
        this.oAuth2UserRequest = oAuth2UserRequest;
        this.registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        this.oAuth2User = loadUser(oAuth2UserRequest);
    }
    abstract String getEmail(Map<String, Object> attributes);
    public Map<String, Object> makeUserAttributes(){
        Map<String, Object> userAttributes = new HashMap<>();
        Map<String, Object> oAuth2Attributes = oAuth2User.getAttributes();

        userAttributes.put("email", getEmail(oAuth2Attributes));
        userAttributes.put("registrationId", this.registrationId);
        userAttributes.put(loadUserNameAttributeName(), oAuth2User.getName());
        return userAttributes;
    }
    public String loadUserNameAttributeName(){
        return oAuth2UserRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
    }

}
