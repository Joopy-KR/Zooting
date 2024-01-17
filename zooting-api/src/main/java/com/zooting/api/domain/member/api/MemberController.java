package com.zooting.api.domain.member.api;

import com.zooting.api.domain.member.application.MemberService;
import com.zooting.api.domain.member.dto.request.MemberReq;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/nickname/check/{nickname}")
    public ResponseEntity<BaseResponse<Boolean>> checkNicknameDuplicate(@RequestParam(name="nickname") String nickname) {
        var result =  memberService.existNickname(nickname);
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                result
        );
    }

    @PatchMapping("/")
    public ResponseEntity<BaseResponse<String>> saveAdditionalInfo(@RequestBody MemberReq memberReq) {
        memberService.updateMemberInfo(memberReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                String.format("추가 정보 저장 성공")
        );
    }


}
