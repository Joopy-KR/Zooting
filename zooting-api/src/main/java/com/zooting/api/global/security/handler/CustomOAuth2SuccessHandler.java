package com.zooting.api.global.security.handler;

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
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        log.info("OAuth2 로그인 성공, onAuthenticationSuccess 호출");
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String userEmail = oAuth2User.getEmail();
        Collection<String> userPrivileges = oAuth2User.getAuthorities().stream().map(Object::toString).toList();

        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail(), userPrivileges);
        log.info("OAuth2SuccessHandler에서 액세스 토큰 발급: " + accessToken);

        UriComponentsBuilder uriComponentsBuilder;
        // 토큰 분기처리
        if(isAnonymousUser(userPrivileges)){
            log.info(userEmail + "는 추가 정보가 없는 유저");
            //TODO 추가 정보 기입 페이지 Redirect
            uriComponentsBuilder = UriComponentsBuilder.fromUriString("http://localhost:5173/login")
                            .queryParam("access-token", accessToken);

        } else {
            log.info(userEmail + "는 추가 정보가 있는 유저");
            uriComponentsBuilder = UriComponentsBuilder.fromUriString("http://localhost:5173/login")
                    .queryParam("access-token", accessToken);
        }
        String redirectURI = uriComponentsBuilder.toUriString();
        response.sendRedirect(redirectURI);
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