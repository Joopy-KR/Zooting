package com.zooting.api.domain.member.api;

import com.zooting.api.domain.block.entity.Block;
import com.zooting.api.domain.member.application.MemberService;
import com.zooting.api.domain.member.dto.request.*;
import com.zooting.api.domain.member.dto.response.MemberRes;
import com.zooting.api.domain.member.dto.response.PointRes;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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

    @PutMapping("/characters")
    public ResponseEntity<BaseResponse<String>> updatePersonality(@RequestBody PersonalityReq personalityReq) {
        memberService.updatePersonality(personalityReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                String.format("성격 수정 완료")
        );
    }

    @PostMapping("/block")
    public ResponseEntity<BaseResponse<String>> saveBlockMember(@RequestBody BlockReq blockReq) {
        memberService.insertBlockList(blockReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                String.format("멤버 차단 완료")
        );
    }
    @DeleteMapping("/block")
    public ResponseEntity<BaseResponse<String>> deleteBlockMember(@RequestBody BlockReq blockReq) {
        memberService.deleteBlock(blockReq);
        return BaseResponse.success(
                SuccessCode.DELETE_SUCCESS,
                String.format("멤버 차단 해제 완료")
        );
    }

    @PostMapping("/reports")
    public ResponseEntity<BaseResponse<String>> insertReport(@RequestBody ReportReq reportReq) {
        memberService.insertReport(reportReq);
        return BaseResponse.success(
                SuccessCode.INSERT_SUCCESS,
                String.format(reportReq.email() + "에 대한 신고 완료")
        );
    }
    @GetMapping("/points")
    public ResponseEntity<BaseResponse<PointRes>> findPoints(@RequestParam(name="nickname") String nickname){
        PointRes result = memberService.findPoints(nickname);
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                result
        );
    }

    @PostMapping("/animal")
    public ResponseEntity<BaseResponse<String>> buyAnimalChangeItem(@RequestBody IntroduceReq introduceReq) {
        Long price = 300L; // 동물상 변경권 가격
        System.out.println("=================================");
        System.out.println(introduceReq.email());
        boolean isDeducted = memberService.deductPoints(introduceReq.email(), price);
        if (isDeducted) {
            return BaseResponse.success(
                    SuccessCode.UPDATE_SUCCESS,
                    String.format("변경 허용")
            );
        }else {
            return BaseResponse.success(
                    SuccessCode.UPDATE_SUCCESS,
                    String.format("변경 불가")
            );
        }

    }
}
