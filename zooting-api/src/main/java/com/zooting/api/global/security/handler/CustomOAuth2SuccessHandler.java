package com.zooting.api.global.security.handler;

import com.zooting.api.domain.member.application.MemberService;
import com.zooting.api.domain.member.entity.Privilege;
import com.zooting.api.global.jwt.JwtService;
import com.zooting.api.global.security.user.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final MemberService memberService;

    /**
     *
     * @param request 유저의 Request
     * @param response 서버의 Response
     * @param authentication
     * @throws IOException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {

        // OAuth2 필터에서 담겨져 온 유저 인증 정보 (Attributes = {Email, Provider, userNameAttributeName}
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String userEmail = (String) oAuth2User.getAttributes().get("email");



        // 여기서 우리 쪽 유저로 전환
        // 1. 유저 정보가 없을 경우
        // -> 이메일 등록 후 익명 유저로 전환
        // 2. 유저 정보가 Anonymous일 경우
        // 3. 아닐 경우



        String accessToken = jwtService.createAccessToken(userEmail, userPrivileges);
        log.info("OAuth2SuccessHandler에서 액세스 토큰 발급: " + accessToken);

        UriComponentsBuilder uriComponentsBuilder;
        // 토큰 분기처리
        if(isAnonymousUser(userPrivileges)){
            // TODO
            //  1. 링크 나올 시 추가 정보 기입 페이지 Redirect
            uriComponentsBuilder = UriComponentsBuilder.fromUriString("http://localhost:5173/addtionalInformation")
                            .queryParam("access-token", accessToken);

        } else {
            uriComponentsBuilder = UriComponentsBuilder.fromUriString("http://localhost:5173/login")
                    .queryParam("access-token", accessToken);
        }
        String redirectURI = uriComponentsBuilder.toUriString();
        response.sendRedirect(redirectURI);
    }

    protected boolean isAnonymousUser(Collection<String> userPrivileges){
        for(String privilege : userPrivileges){
            if(Privilege.ANONYMOUS.name().equals(privilege)){
                return true;
            }
        }
        return false;
    }
}