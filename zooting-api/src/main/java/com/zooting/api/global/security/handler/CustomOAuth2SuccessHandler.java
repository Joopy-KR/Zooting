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

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String userEmail = oAuth2User.getEmail();
        Collection<String> userPrivileges = oAuth2User.getAuthorities().stream().map(Object::toString).toList();

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