package com.zooting.api.global.security;

import com.zooting.api.domain.member.entity.Privilege;
import com.zooting.api.domain.member.service.MemberService;
import com.zooting.api.global.jwt.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final MemberService memberService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 로그인 성공, onAuthenticationSuccess 호출");
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // 나중에 권한 담을수 있게 수정
        boolean isAnonymous = oAuth2User.getAuthorities().stream()
                .map(Object::toString)
                .anyMatch(authority -> authority.equals(Privilege.ANONYMOUS.name()));

        if(isAnonymous){
            String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
            response.addHeader("Authorization", "Bearer " + accessToken);
            // 추가 정보 기입 페이지로 보내는 로직 추가
        } else {

        }
    }
}
