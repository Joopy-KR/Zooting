package com.zooting.api.global.security.service;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.domain.member.service.MemberService;
import com.zooting.api.global.security.CustomOAuth2User;
import com.zooting.api.global.security.OAuth2Attributes;
import com.zooting.api.global.security.SocialType;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        log.info("Oauth2 로그인에 성공했다.");

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId(); // 로그인한 플랫폼
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
        Optional<Member> member = memberService.getMemberByEmail(oAuth2Attributes.getOAuth2UserInfo().getEmail());
        return member.orElseGet(() -> saveMember(oAuth2Attributes));
    }

    private Member saveMember(OAuth2Attributes oAuth2Attributes){
        Member member = oAuth2Attributes.toEntity(oAuth2Attributes.getOAuth2UserInfo());
        return memberService.registerMember(member);
    }
}