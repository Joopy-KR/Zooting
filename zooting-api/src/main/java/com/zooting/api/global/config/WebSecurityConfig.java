package com.zooting.api.global.config;

import com.zooting.api.global.jwt.JwtAuthenticateFilter;
import com.zooting.api.global.jwt.service.JwtService;
import com.zooting.api.global.security.handler.CustomOAuth2FailHandler;
import com.zooting.api.global.security.handler.CustomOAuth2SuccessHandler;
import com.zooting.api.global.security.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private static final String[] URL_WHITE_LIST = {"/error", "/login", "/favicon.ico",
            "/health", "/api-docs/**", "/swagger-ui/**",
            "/swagger-resources/**", "/swagger-ui.html", "/api/token/**",
            "/ws/dm/**", "/api/sub/**", "/api/pub/**"
    };
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2FailHandler customOAuth2FailHandler;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final JwtService jwtService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
                .cors(corsConfig -> corsConfig.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                 JWT 토큰을 쿠키에 넣을지, LocalStorage에 넣을지에 따라 비활성화 여부 결정
                .csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(
                        authorize -> authorize.requestMatchers(URL_WHITE_LIST).permitAll().anyRequest().authenticated())
                .oauth2Login(
                        oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                                .successHandler(customOAuth2SuccessHandler).failureHandler(customOAuth2FailHandler))
                .addFilterBefore(jwtAuthenticateFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticateFilter jwtAuthenticateFilter() {
        return new JwtAuthenticateFilter(jwtService, URL_WHITE_LIST);
    }

    // CORS 설정
    CorsConfigurationSource corsConfigurationSource() {
        final List<String> allowedHeaders = List.of("*");
        final List<String> allowedOriginPatterns = List.of(
                "http://localhost:8080",
                "http://localhost:5173",
                "https://i10a702.p.ssafy.io",
                "http://70.12.247.212:5173"
        );
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(allowedHeaders);
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(allowedOriginPatterns); // ⭐️ 허용할 origin
            config.setAllowCredentials(true);
            return config;
        };
    }
}