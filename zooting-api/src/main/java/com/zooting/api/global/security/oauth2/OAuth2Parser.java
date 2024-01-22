package com.zooting.api.global.security.user;
import java.util.Map;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Component;

@Component
public class OAuth2Parser {
    public String getRegistrationId(OAuth2UserRequest oAuth2UserRequest){
        return oAuth2UserRequest.getClientRegistration().getRegistrationId();
    }
    public OAuth2Provider getProvider(OAuth2UserRequest oAuth2UserRequest) {
        String registrationId = getRegistrationId(oAuth2UserRequest);
        for (OAuth2Provider provider : OAuth2Provider.values()) {
            if (registrationId.equals(provider.getName()))
                return provider;
        }
        //TODO
        // null 발생시 에러 처리
        return null;
    }

    public String getEmail(Map<String, Object> attributes, OAuth2Provider provider){
        if(provider.equals(OAuth2Provider.KAKAO)){
            return getKakaoEmail(attributes);
        }
        //TODO
        // 다른 소셜 로그인 플랫폼 추가시 메소드 추가
        return null;
    }


    public String getNameAttributeName(OAuth2UserRequest oAuth2UserRequest){
        return oAuth2UserRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
    }

    private String getKakaoEmail(Map<String, Object> attributes){
        Map<String, Object> accountInfo = (Map<String, Object>) attributes.get("kakao_account");
        return accountInfo.get("email").toString();
    }
}
