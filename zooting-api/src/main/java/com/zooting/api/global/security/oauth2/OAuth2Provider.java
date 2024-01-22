package com.zooting.api.global.security.user;

public enum OAuth2Provider {
    NAVER, KAKAO, GOOGLE;

    public String getName(){
        return this.name().toLowerCase();
    }
}
