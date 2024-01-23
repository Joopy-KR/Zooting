package com.zooting.api.global.security.handler;

import com.zooting.api.domain.member.entity.Privilege;
import com.zooting.api.global.jwt.JwtService;
import com.zooting.api.global.security.oauth2.CustomOAuth2User;
import com.zooting.api.global.security.userdetails.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {

        // Authentication은 OAuth2 Service의 loaduser에서 담겨져 온 유저 인증 정보
        // Attributes = {이메일, 소셜 로그인 Provider, userNameAttributeName}
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        String accessToken = jwtService.createAccessToken(userDetails);

        UriComponentsBuilder uriComponentsBuilder;

        if(isAnonymousMember(userDetails)){
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
    public boolean isAnonymousMember(UserDetails userDetails){
        return userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Privilege.ANONYMOUS.name()));
    }
}