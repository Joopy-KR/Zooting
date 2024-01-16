package com.zooting.api.global.security;
import java.util.Map;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
    @Service
    public class OAuth2UserService extends DefaultOAuth2UserService {
        @Override
        public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
            OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
            String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId(); // 로그인한 플랫폼

            String userNameAttributeName = oAuth2UserRequest.getClientRegistration()
                    .getProviderDetails()
                    .getUserInfoEndpoint()
                    .getUserNameAttributeName();

            /*
            유저 번호(id), 로그인 시간, kakao_account 반환
             */
            Map<String, Object> oAuth2UserAttributes = oAuth2User.getAttributes();

            /*
            kakak_account에 있는 유저 정보 반환(이메일 소유 여부, 타당한 이메일 여부, 이메일 등)
             */
            Map<String, Object> kakaoAccountInfo = (Map<String, Object>) oAuth2UserAttributes.get("kakao_account");

            /*
            kakaoAccount에 있는 유저 이메일 반환
             */
            String userEmail = (String) kakaoAccountInfo.get("email");



            /*
            1. 최초 로그인 시 -> 유저 데이터(Email)를 DB에 저장한다.
               추가 정보 기입 페이지로 Redirect 시킨다.

               유저 데이터 가져오는 메소드 만들기
               유저 데이터 가져와서 담을 메소드 만들기


            2. 기존 회원일 경우 -> DB에 있는 유저 Email을 확인하고 JWT Token을 부여한다.
             */
            return oAuth2User;
        }






    }