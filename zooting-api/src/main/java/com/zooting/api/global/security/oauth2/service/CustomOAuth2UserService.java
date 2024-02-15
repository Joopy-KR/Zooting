package com.zooting.api.global.security.oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final OAuth2RequestProcessorFactory oAuth2RequestProcessorFactory;
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        log.trace("1. OAuth2 로그인에 성공했습니다.");
        log.trace("2. OAuth2 리소스 서버로부터 유저 정보를 가져옵니다.");
        OAuth2RequestProcessor oAuth2Parser = oAuth2RequestProcessorFactory.createOAuth2Processor(oAuth2UserRequest);

        return new DefaultOAuth2User(
                AuthorityUtils.NO_AUTHORITIES,
                oAuth2Parser.makeUserAttributes(),
                oAuth2Parser.loadUserNameAttributeName()
        );
    }

}