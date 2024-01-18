package com.zooting.api.global.security;

import com.zooting.api.domain.member.entity.Privilege;
import com.zooting.api.domain.member.service.MemberService;
import com.zooting.api.global.jwt.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

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

        String userEmail = oAuth2User.getEmail();
        Collection<String> userPrivileges = oAuth2User.getAuthorities().stream().map(Object::toString).toList();

        // 토큰 분기처리
        if(isAnonymousUser(userPrivileges)){
            log.info(userEmail + "는 추가 정보가 없는 유저");
            String accessToken = jwtService.createAccessToken(oAuth2User.getEmail(), userPrivileges);

            log.info("OAuth2SuccessHandler에서 액세스 토큰 발급: " + accessToken);
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString("/login")
                            .queryParam("access-token", accessToken);

            String redirectURI = uriComponentsBuilder.toUriString();
            response.sendRedirect(redirectURI);
            // 추가 정보 기입 페이지로 보내는 로직 추가
        } else {
            log.info(userEmail + "는 추가 정보가 있는 유저");

        }
    }

    protected boolean isAnonymousUser(Collection<String> userPrivileges){
        log.info("추가 정보 기입 여부 검증중");
        for(String privilege : userPrivileges){
            if(Privilege.ANONYMOUS.name().equals(privilege)){
                return true;
            }
        }
        return false;
    }
}