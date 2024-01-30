package com.zooting.api.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.zooting.api.domain.member.entity.Privilege;
import com.zooting.api.global.jwt.service.JwtCreator;
import com.zooting.api.global.security.userdetails.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
public class AccessWithJwtTest {
    @Autowired
    private MockMvc mvc;

    private final JwtCreator jwtCreator;
    @Autowired
    public AccessWithJwtTest(JwtCreator jwtCreator) {
        this.jwtCreator = jwtCreator;
    }

    @Test
    public void anonymousUserWithJwtTest() throws Exception {
        CustomUserDetails userDetails = CustomUserDetails.builder()
                .authorities(AuthorityUtils.createAuthorityList(Privilege.USER.name()))
                .email("psnew14@naver.com")
                .nickname("박상현")
                .build();

        String accessToken = jwtCreator.createAccessToken(userDetails);
        mvc
                .perform(get("http://localhost:8080/api/members/info")
                        .param("nickname", "tttt")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().is(201)).andDo(print());

    }
}
