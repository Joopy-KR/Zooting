package com.zooting.api.global.security;

import java.util.Map;

public abstract class OAuth2UserInfo {
    Map<String, Object> attributes;
    protected OAuth2UserInfo(Map<String, Object> attributes){
        this.attributes = attributes;
    }
    public abstract String getEmail();
}
