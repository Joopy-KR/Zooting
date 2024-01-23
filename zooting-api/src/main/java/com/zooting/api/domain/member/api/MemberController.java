package com.zooting.api.domain.member.api;

import com.zooting.api.domain.block.entity.Block;
import com.zooting.api.domain.member.application.MemberService;
import com.zooting.api.domain.member.dto.request.*;
import com.zooting.api.domain.member.dto.response.MemberRes;
import com.zooting.api.domain.member.dto.response.PointRes;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name= "유저", description = "Member 관련 API")
public class MemberController {
    private final MemberService memberService;

    @Operation(
            summary = "닉네임 중복 체크",
            description = "닉네임이 중복될 때 true반환, " +
                    "닉네임 중복되지 않을 때 false 반환"
    )
    @GetMapping("/nickname/check")
    public ResponseEntity<BaseResponse<Boolean>> checkNicknameDuplicate(@RequestParam(name = "nickname") String nickname) {
        var result = memberService.existNickname(nickname);
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                result
        );
    }

    @Operation(
            summary = "로그인 후 추가 정보가 저장 여부 확인",
            description = "nickname 여부로 판단. " +
                    "false - 추가 정보 저장되지 않음" +
                    "true - 추가 정보 저장되어 있음")
    @GetMapping("/additional/check")
    public ResponseEntity<BaseResponse<Boolean>> checkAdditionalInfo(@AuthenticationPrincipal UserDetails userDetails) {
        var result = memberService.checkAdditionalInfo(userDetails.getUsername());
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                result
        );
    }
    @Operation(summary = "로그인 후 추가 정보 저장")
    @PreAuthorize("hasAnyRole('ANONYMOUS')")
    @PutMapping
    public ResponseEntity<BaseResponse<String>> saveAdditionalInfo(
            @Valid @RequestBody MemberReq memberReq,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws ParseException {
        memberService.updateMemberInfo(userDetails.getUsername(), memberReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "추가 정보 저장 성공"
        );
    }

    @Operation(summary = "관심사, 이상형 수정")
    @PreAuthorize("hasAnyRole('ANONYMOUS', 'USER')")
    @PutMapping("/interests")
    public ResponseEntity<BaseResponse<String>> updateInterests(
            @Valid @RequestBody InterestsReq interestsReq,
            @AuthenticationPrincipal UserDetails userDetails) {
        memberService.updateInterestsandIdeal(userDetails.getUsername(), interestsReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "관심사, 이상형 수정 완료"
        );
    }

    @Operation(summary = "자기소개 수정")
    @PreAuthorize("hasAnyRole('ANONYMOUS', 'USER')")
    @PutMapping("/introduce")
    public ResponseEntity<BaseResponse<String>> updateIntroduce(
            @Valid @RequestBody IntroduceReq introduceReq,
            @AuthenticationPrincipal UserDetails userDetails) {
        memberService.updateIntroduce(userDetails.getUsername(), introduceReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "자기소개 수정 완료"
        );
    }
    @Operation(
            summary = "유저 검색",
            description = "검색한 키워드에 해당하는 멤버 중 나를 차단한 사람 제외하고 리스트로 반환"
    )
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/members")
    public ResponseEntity<BaseResponse<List<MemberRes>>> findMemberList(
            @RequestParam(name = "nickname") String nickname,
            @AuthenticationPrincipal UserDetails userDetails) {
        List<MemberRes> memberResList = memberService.findMemberList(userDetails.getUsername(), nickname);
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                memberResList
        );
    }
    @Operation(summary = "성격 수정")
    @PreAuthorize("hasAnyRole('ANONYMOUS', 'USER')")
    @PutMapping("/characters")
    public ResponseEntity<BaseResponse<String>> updatePersonality(
            @Valid @RequestBody PersonalityReq personalityReq,
            @AuthenticationPrincipal UserDetails userDetails) {
        memberService.updatePersonality(userDetails.getUsername(), personalityReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "성격 수정 완료"
        );
    }


    @Operation(summary = "내 포인트 조회")
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
    @Operation(
            summary = "동물변경권 구매",
            description = "포인트 부족 시에는 변경 불가")
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
        }
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "변경 불가"
        );


    }
    @Operation(
            summary = "닉네임 변경권 구매",
            description = "포인트 부족시 닉네임 변경 불가"
    )
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
        }
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "변경 불가"
        );

    }

    @Operation(summary = "내 이메일 조회")
    @GetMapping
    public ResponseEntity<Member> getMemberByEmail(@RequestParam(name = "email") String email) {
        return ResponseEntity.ok(memberService.getMemberByEmail(email));
    }

    @Operation(summary = "이메일 등록")
    @PostMapping("/register/email")
    public ResponseEntity<Member> initialMemberRegister(@RequestBody String email) {
        return ResponseEntity.ok(memberService.initialMemberRegister(email));
    }
}
