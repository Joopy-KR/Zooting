package com.zooting.api.domain.member.controller;

import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.domain.member.service.MemberService;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;
    MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @GetMapping("/email")
    public ResponseEntity<Member> getMemberByEmail(@RequestParam String email){
        Optional<Member> member = memberService.getMemberByEmail(email);
        return member.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/email")
    public ResponseEntity<Member> registerMemberByEmail(@RequestBody String email){
        Member member = memberService.registerMemberByEmail(email);
        return ResponseEntity.ok(member);
    }

    @GetMapping("/hello")
    public String helloTest(){
        return "로그인 잘 했네";
    }
}
