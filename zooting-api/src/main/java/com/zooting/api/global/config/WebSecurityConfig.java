package com.zooting.api.global.config;

import com.zooting.api.global.jwt.JwtAuthenticateFilter;
import com.zooting.api.global.jwt.JwtService;
import com.zooting.api.global.security.handler.CustomOAuth2SuccessHandler;
import com.zooting.api.global.security.service.CustomOAuth2UserService;
import java.net.URL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final JwtService jwtService;
    private static final String[] URL_WHITE_LIST = {"/error", "/login", "/favicon.ico", "/health", "/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/swagger-ui.html"};

    WebSecurityConfig(
            CustomOAuth2UserService customOAuth2UserService,
            CustomOAuth2SuccessHandler customOAuth2SuccessHandler, JwtService jwtService) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                 JWT 토큰을 쿠키에 넣을지, LocalStorage에 넣을지에 따라 비활성화 여부 결정
//                 .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(URL_WHITE_LIST).permitAll()
                        .anyRequest().authenticated())

                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                        .successHandler(customOAuth2SuccessHandler)
                )
                .addFilterBefore(jwtAuthenticateFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public JwtAuthenticateFilter jwtAuthenticateFilter() {
        return new JwtAuthenticateFilter(jwtService, URL_WHITE_LIST);
    }
}