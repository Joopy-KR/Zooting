package com.zooting.api.global.security.oauth2.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class OAuth2RequestProcessorFactory {
    /**
     * @param oAuth2UserRequest OAuth2 리소스 서버의 정보와 유저의 정보가 담긴 Request
     * @return OAuth2 리소스 서버의 소문자 플랫폼 이름 ("kakao", "naver", "google" 등)에 따라
     */

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public OAuth2RequestProcessor createOAuth2Processor(OAuth2UserRequest oAuth2UserRequest){
        log.info("3. 유저가 로그인한 플랫폼을 판별합니다");
        String oAuth2registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        log.info("4. 유저가 로그인한 플랫폼은 '" + oAuth2registrationId + "'입니다.");

        try {
            return switch (oAuth2registrationId) {
                case "kakao" -> new KakaoOAuth2Processor(oAuth2UserRequest);
                default -> throw new IllegalArgumentException("유효하지 않은 소셜 플랫폼입니다.");
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}