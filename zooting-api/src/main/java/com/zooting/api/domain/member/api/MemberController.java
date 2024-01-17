package com.zooting.api.domain.member.api;

import com.zooting.api.domain.member.application.MemberService;
import com.zooting.api.domain.member.dto.request.InterestsReq;
import com.zooting.api.domain.member.dto.request.IntroduceReq;
import com.zooting.api.domain.member.dto.request.MemberReq;
import com.zooting.api.domain.member.dto.response.MemberRes;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/nickname/check")
    public ResponseEntity<BaseResponse<Boolean>> checkNicknameDuplicate(@RequestParam(name="nickname") String nickname) {
        var result =  memberService.existNickname(nickname);
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                result
        );
    }

    @PutMapping("/")
    public ResponseEntity<BaseResponse<String>> saveAdditionalInfo(@RequestBody MemberReq memberReq) throws ParseException {
        memberService.updateMemberInfo(memberReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                String.format("추가 정보 저장 성공")
        );
    }

    @PutMapping("/interests")
    public ResponseEntity<BaseResponse<String>> updateInterests(@RequestBody InterestsReq interestsReq) {
        memberService.updateInterestsandIdeal(interestsReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                String.format("관심사, 이상형 수정 완료")
        );
    }

    @PutMapping("/introduce")
    public ResponseEntity<BaseResponse<String>> updateIntroduce(@RequestBody IntroduceReq introduceReq) {
        memberService.updateIntroduce(introduceReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                String.format("자기소개 수정 완료")
        );
    }

    @GetMapping("")
    public ResponseEntity<BaseResponse<List<MemberRes>>>  findMemberList(@RequestParam(name="email") String email, @RequestParam(name="nickname") String nickname) {
        List<MemberRes> memberResList = memberService.findMemberList(email, nickname);
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                memberResList
        );
    }
}
