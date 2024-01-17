package com.zooting.api.global.security;

import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.domain.member.entity.Privilege;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2Attributes {
    private final String nameAttributeKey;
    private final OAuth2UserInfo oAuth2UserInfo;

    @Builder
    private OAuth2Attributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo){
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oauth2UserInfo;
    }
    public static OAuth2Attributes of(
            SocialType socialType,
            String userNameAttributeName,
            Map<String, Object> attributes
    ){
        if(socialType == SocialType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        } else {
            return ofGoogle(userNameAttributeName, attributes);
        }
    }

    private static OAuth2Attributes ofKakao(String userNameAttributeName, Map<String, Object> attributes){
        return OAuth2Attributes
                .builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    // TO-Do
    private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes){
        return OAuth2Attributes
                .builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public Member toEntity(OAuth2UserInfo oAuth2UserInfo){
        Member member = Member
                .builder()
                .email(oAuth2UserInfo.getEmail())
                .build();

        member.getRole().add(Privilege.ANONYMOUS);
        return member;
    }
}
