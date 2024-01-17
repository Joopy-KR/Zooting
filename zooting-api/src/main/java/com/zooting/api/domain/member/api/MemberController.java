package com.zooting.api.domain.member.api;

import com.zooting.api.domain.member.application.MemberService;
import com.zooting.api.domain.member.dto.request.MemberAdditionalInfoReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;

@Controller("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/check/{nickname}")
    public Boolean checkNicknameDuplicate(@PathVariable("nickname") String nickname) {
        return memberService.existNickname(nickname);
    }

    @PostMapping("/")
    public ResponseEntity saveAdditionalInfo(@RequestBody MemberAdditionalInfoReq memberAdditionalInfoReq) {
        memberService.saveAdditionalInfo(memberAdditionalInfoReq.email(),
                memberAdditionalInfoReq.gender(),
                memberAdditionalInfoReq.nickname(),
                memberAdditionalInfoReq.birth(),
                memberAdditionalInfoReq.address());
        return ResponseEntity.ok().build();
    }
    

}
