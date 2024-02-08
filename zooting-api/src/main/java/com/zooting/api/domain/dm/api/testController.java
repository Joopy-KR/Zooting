package com.zooting.api.domain.dm.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class testController {

    @GetMapping("/test")
    public String chat() {
        return "/chat.html";
    }

    @GetMapping("/subtest")
    public String subchat() {
        return "/subtest.html";
    }
}
