package com.zooting.api.global.security;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.domain.member.service.MemberService;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId(); // 로그인한 플랫폼
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        Map<String, Object> oAuth2UserAttributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccountInfo = (Map<String, Object>) oAuth2UserAttributes.get("kakao_account");
        String userEmail = (String) kakaoAccountInfo.get("email");

        Optional<Member> a = memberService.getMemberByEmail(userEmail);

        if(a.isEmpty()){
            System.out.println("이메일 \"" + userEmail +"\"을 가진 회원이 없습니다.");
            memberService.registerMember(
                    Member
                            .builder()
                            .email(userEmail)
                            .build()
            );
        } else {
            System.out.println(userEmail + "이 로그인 했다.");
        }

        return oAuth2User;
    }
}