package com.zooting.api.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@AutoConfigureMockMvc
public class AccessWithoutJwtTest {
    @Autowired
    private MockMvc mvc;
    @Test
    public void helloUnauthenticated() throws Exception {
        mvc
                .perform(get("/"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(username = "박상현", authorities = {"ANONYMOUS"})
    public void helloAuthenticated() throws Exception {
        mvc
                .perform(get("/"))
                .andExpect(status().is(403));
    }
}
