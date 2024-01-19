package com.zooting.api.domain.member.controller;

import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @GetMapping("/")
    public ResponseEntity<Member> getMemberByEmail(@RequestParam String email){
        return ResponseEntity.ok(memberService.getMemberByEmail(email));
    }
    @PostMapping("/register/email")
    public ResponseEntity<Member> initialMemberRegister(@RequestBody String email){
        return ResponseEntity.ok(memberService.initialMemberRegister(email));
    }


}
