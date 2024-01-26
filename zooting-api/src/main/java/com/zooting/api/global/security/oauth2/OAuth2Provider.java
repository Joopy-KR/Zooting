package com.zooting.api.global.security.oauth2;

@Deprecated
public enum OAuth2Provider {
    NAVER, KAKAO, GOOGLE;

    public String getName(){
        return this.name().toLowerCase();
    }
}
