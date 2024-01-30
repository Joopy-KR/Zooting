package com.zooting.api.domain.member.api;

import com.zooting.api.domain.member.application.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;


// @WebMvcTest(controllers = MemberController.class)   //mvc 관련 스프링 빈만 등록, MockMvc 자동 설정, 가볍게 테스트할 때 사용
@AutoConfigureMockMvc //모든 스프링 빈 등록, WebMvcTest 없이 MockMvc 사용, 세밀한 제어 가능
//@ExtendWith(SpringExtension.class)  // junit4의 runwith extension 사용 목적
@AutoConfigureRestDocs
@SpringBootTest // 테스트에 필요한 거의 모든 의존성 제공, extendwith 내장
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberService memberService;
    @Test
    @DisplayName("닉네임 중복체크")
    void testCheckNicknameDuplicate() throws Exception {
        mockMvc.perform(get("/api/members/")
                        .param("nickname","나는세진"))
                .andDo(document("check-nickname")) // document 추가
                .andExpect(status().isOk())
                ;
        // given
//        this.mockMvc.perform(
//                MockMvcRequestBuilders.get("/api/members/nickname/check"))
//        given(memberService.)
    }
}