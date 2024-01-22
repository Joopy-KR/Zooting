package com.zooting.api.global.security.service;

import com.zooting.api.domain.member.application.MemberService;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.security.user.CustomOAuth2User;
import com.zooting.api.global.security.user.OAuth2Attributes;
import com.zooting.api.global.security.user.SocialType;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;

    /**
     * 소셜 로그인한 유저의 정보를 받아오는 메소드
     * User, Client, Provider를 구분할 것
     *
     * @param oAuth2UserRequest
     * 클라이언트(Zooting)이 Application.yml에서  설정한 Configuration 정보(= OAuth2 표준 파라미터에 대한 정보)가 담긴 ClientRegistration
     * 클라이언트에서 리소스 서버에 접근하여 유저가 제공 동의한 정보를 볼 수 있는 AccessToken,
     * 토큰 만료 기간 등의 추가 정보가 담긴 AdditionalInfo
     * ClientRegistration
     *
     * @return
     * 소셜 타입 (카카오, 구글, 네이버 등)에 맞게 커스텀한 유저 정보
     */
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        /*
         * registrationId = 소셜 플랫폼 이름 추출
         * userNameAttributeName = 각 소셜 플랫폼에 등록된 유저의 엔드포인트에 접근할 수 있는 Key값의 이름
         */

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);

        String userNameAttributeName = oAuth2UserRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();


        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(socialType, userNameAttributeName, attributes);

        Member member = getMember(oAuth2Attributes);

        return new CustomOAuth2User(
                AuthorityUtils.
                        createAuthorityList(member.getRole().stream().map(Enum::name).collect(Collectors.toList())),
                attributes,
                oAuth2Attributes.getNameAttributeKey(),
                member.getEmail()
        );
    }

    private SocialType getSocialType(String registrationId) {
        String KAKAO = "kakao";
        String GOOGLE = "google";

        if (KAKAO.equals(registrationId)) {
            return SocialType.KAKAO;
        } else if(GOOGLE.equals(registrationId)){
            return SocialType.GOOGLE;
        }
        return SocialType.NAVER;
    }

    private Member getMember(OAuth2Attributes oAuth2Attributes){
        Optional<Member> member = memberService.
                checkRegisteredMember(oAuth2Attributes.getOAuth2UserInfo().getEmail());
        return member.orElseGet(() -> saveMember(oAuth2Attributes));
    }

    private Member saveMember(OAuth2Attributes oAuth2Attributes){
        return memberService.initialMemberRegister(
                oAuth2Attributes.getOAuth2UserInfo().getEmail()
        );
    }
}