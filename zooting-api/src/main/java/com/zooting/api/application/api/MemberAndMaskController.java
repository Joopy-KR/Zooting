package com.zooting.api.application.api;

import com.zooting.api.application.dto.request.MemberAndMaskReq;
import com.zooting.api.application.dto.response.MemberAndBackgroundRes;
import com.zooting.api.application.dto.response.MemberAndMaskRes;
import com.zooting.api.application.usecase.MemberAndMaskUsecase;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mask")
@RequiredArgsConstructor
@Tag(name="멤버, 마스크", description = "멤버와 마스크 관련 API")
public class MemberAndMaskController {
    private final MemberAndMaskUsecase memberAndMaskUsecase;
    @PostMapping
    @Operation(summary = "마스크 구매",
            description = "포인트 부족 / 이미 보유한 마스크/ 유저 동물상과 불일치 시 구매 실패"
    )
     public ResponseEntity<BaseResponse<String>> buyMask(
        @RequestBody MemberAndMaskReq maskReq,
        @AuthenticationPrincipal UserDetails userDetails) {

        Boolean buyMyMask = memberAndMaskUsecase.buyMask(userDetails.getUsername(), maskReq);
        if (buyMyMask) {
            return BaseResponse.success(
                    SuccessCode.UPDATE_SUCCESS,
                    "구매 완료"
            );
        }else {
            return BaseResponse.success(
                    SuccessCode.UPDATE_SUCCESS,
                    "구매 실패 - 잔여 포인트 부족 / 이미 보유한 마스크이미지 / 유저의 동물상과 일치하지 않음"
            );
        }


    }
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping
    public ResponseEntity<BaseResponse<List<MemberAndMaskRes>>> findAllMaskInventory(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<MemberAndMaskRes> result = memberAndMaskUsecase.findAllMaskInventory(userDetails.getUsername());
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                result
        );
    }

}
