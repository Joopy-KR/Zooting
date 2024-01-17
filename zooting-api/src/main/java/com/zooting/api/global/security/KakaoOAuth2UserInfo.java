package com.zooting.api.global.security;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
    KakaoOAuth2UserInfo(Map<String, Object> attributes){
        super(attributes);
    }
    @Override
    public String getEmail() {
        Map<String, Object> accountInfo = (Map<String, Object>) attributes.get("kakao_account");
        return (String) accountInfo.get("email");
    }
}
