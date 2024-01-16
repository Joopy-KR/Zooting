package com.zooting.api.domain.member.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberTestController {
    @RequestMapping("/")
    public String indexController(){
        return "반갑다잇";
    }
    @RequestMapping("/hello")
    public String helloController(){
        return "로그인 한 사람만 들어올 수 있는 곳";
    }

}
