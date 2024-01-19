package com.zooting.api.domain.member.api;

import com.zooting.api.domain.block.entity.Block;
import com.zooting.api.domain.member.application.MemberService;
import com.zooting.api.domain.member.dto.request.*;
import com.zooting.api.domain.member.dto.response.MemberRes;
import com.zooting.api.domain.member.dto.response.PointRes;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/nickname/check")
    public ResponseEntity<BaseResponse<Boolean>> checkNicknameDuplicate(@RequestParam(name = "nickname") String nickname) {
        var result = memberService.existNickname(nickname);
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                result
        );
    }

    @PreAuthorize("hasAnyRole('ANONYMOUS')")
    @PutMapping("/")
    public ResponseEntity<BaseResponse<String>> saveAdditionalInfo(
            @RequestBody MemberReq memberReq,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws ParseException {
        memberService.updateMemberInfo(userDetails.getUsername(), memberReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "추가 정보 저장 성공"
        );
    }

    @PreAuthorize("hasAnyRole('ANONYMOUS', 'USER')")
    @PutMapping("/interests")
    public ResponseEntity<BaseResponse<String>> updateInterests(
            @RequestBody InterestsReq interestsReq,
            @AuthenticationPrincipal UserDetails userDetails) {
        memberService.updateInterestsandIdeal(userDetails.getUsername(), interestsReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "관심사, 이상형 수정 완료"
        );
    }

    @PreAuthorize("hasAnyRole('ANONYMOUS', 'USER')")
    @PutMapping("/introduce")
    public ResponseEntity<BaseResponse<String>> updateIntroduce(
            @RequestBody IntroduceReq introduceReq,
            @AuthenticationPrincipal UserDetails userDetails) {
        memberService.updateIntroduce(userDetails.getUsername(), introduceReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "자기소개 수정 완료"
        );
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("")
    public ResponseEntity<BaseResponse<List<MemberRes>>> findMemberList(
            @RequestParam(name = "nickname") String nickname,
            @AuthenticationPrincipal UserDetails userDetails) {
        List<MemberRes> memberResList = memberService.findMemberList(userDetails.getUsername(), nickname);
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                memberResList
        );
    }
    @PreAuthorize("hasAnyRole('ANONYMOUS', 'USER')")
    @PutMapping("/characters")
    public ResponseEntity<BaseResponse<String>> updatePersonality(
            @RequestBody PersonalityReq personalityReq,
            @AuthenticationPrincipal UserDetails userDetails) {
        memberService.updatePersonality(userDetails.getUsername(), personalityReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "성격 수정 완료"
        );
    }
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/block")
    public ResponseEntity<BaseResponse<String>> saveBlockMember(
            @RequestBody BlockReq blockReq,
            @AuthenticationPrincipal UserDetails userDetails) {
        memberService.insertBlockList(userDetails.getUsername(), blockReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "멤버 차단 완료"
        );
    }
    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("/block")
    public ResponseEntity<BaseResponse<String>> deleteBlockMember(
            @RequestBody BlockReq blockReq,
            @AuthenticationPrincipal UserDetails userDetails) {
        memberService.deleteBlock(userDetails.getUsername(), blockReq);
        return BaseResponse.success(
                SuccessCode.DELETE_SUCCESS,
                "멤버 차단 해제 완료"
        );
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/reports")
    public ResponseEntity<BaseResponse<String>> insertReport(
            @RequestBody ReportReq reportReq,
            @AuthenticationPrincipal UserDetails userDetails) {
        memberService.insertReport(userDetails.getUsername(), reportReq);
        return BaseResponse.success(
                SuccessCode.INSERT_SUCCESS,
                reportReq.email() + "에 대한 신고 완료"
        );
    }
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/points")
    public ResponseEntity<BaseResponse<PointRes>> findPoints(
            @RequestParam(name = "nickname") String nickname,
            @AuthenticationPrincipal UserDetails userDetails) {
        PointRes result = memberService.findPoints(userDetails.getUsername());
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                result
        );
    }
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/animal")
    public ResponseEntity<BaseResponse<String>> buyAnimalChangeItem(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long price = 300L; // 동물상 변경권 가격
        boolean isDeducted = memberService.deductPoints(userDetails.getUsername(), price);
        if (isDeducted) {
            return BaseResponse.success(
                    SuccessCode.UPDATE_SUCCESS,
                    "변경 허용"
            );
        } else {
            return BaseResponse.success(
                    SuccessCode.UPDATE_SUCCESS,
                    "변경 불가"
            );
        }

    }
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/nickname")
    public ResponseEntity<BaseResponse<String>> buyNicknameChangeItem(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long price = 600L; // 닉네임 변경권 가격
        boolean isDeducted = memberService.deductPoints(userDetails.getUsername(), price);
        if (isDeducted) {
            return BaseResponse.success(
                    SuccessCode.UPDATE_SUCCESS,
                    "변경 허용"
            );
        } else {
            return BaseResponse.success(
                    SuccessCode.UPDATE_SUCCESS,
                    "변경 불가"
            );
        }
    }

    @GetMapping("/")
    public ResponseEntity<Member> getMemberByEmail(@RequestParam String email) {
        return ResponseEntity.ok(memberService.getMemberByEmail(email));
    }

    @PostMapping("/register/email")
    public ResponseEntity<Member> initialMemberRegister(@RequestBody String email) {
        return ResponseEntity.ok(memberService.initialMemberRegister(email));
    }
}
