package com.zooting.api.global.security.oauth2.service;

import java.util.Map;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

public class KakaoOAuth2Processor extends OAuth2RequestProcessor {
    public KakaoOAuth2Processor(OAuth2UserRequest oAuth2UserRequest){
        super(oAuth2UserRequest);
    }
    @Override
    public String getEmail(Map<String, Object> attributes){
        Object accountInfo = attributes.get("kakao_account");
        if(accountInfo instanceof Map<?, ?>){
            Object email = ((Map<?, ?>) accountInfo).get("email");
            if(email instanceof String){
                return (String) email;
            }
        }
        return null;
    }
}