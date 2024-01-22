package com.zooting.api.application.api;

import com.zooting.api.application.dto.request.MemberAndBackgroundReq;
import com.zooting.api.application.dto.response.MemberAndBackgroundRes;
import com.zooting.api.application.usecase.MemberAndBackgroundUsecase;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backgrounds")
@RequiredArgsConstructor
@Tag(name="멤버와 배경", description = "멤버와 배경이미지 관련 API")
public class MemberAndBackgroundController {
    private final MemberAndBackgroundUsecase memberAndBackgroundUsecase;

    @Operation(
            summary = "배경 이미지 구매",
            description = "포인트가 부족하거나 이미 보유한 배경이라면 구매 불가")
    @PostMapping
    public ResponseEntity<BaseResponse<String>> buyBackgroundImg(
            @Valid @RequestBody MemberAndBackgroundReq backgroundReq,
            @AuthenticationPrincipal UserDetails userDetails) {
        Boolean buyBGImg = memberAndBackgroundUsecase.buyBackgroundImg(userDetails.getUsername(), backgroundReq);
        if (buyBGImg) {
            return BaseResponse.success(
                    SuccessCode.UPDATE_SUCCESS,
                    "구매 완료"
            );
        }
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "구매 실패 - 잔여 포인트 부족 혹은 이미 보유한 배경이미지"
        );

    }
    @Operation(summary = "해금 배경 조회")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping
    public ResponseEntity<BaseResponse<List<MemberAndBackgroundRes>>> findAllBackgroundInventory(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<MemberAndBackgroundRes> result = memberAndBackgroundUsecase.findAllBackgroundInventory(userDetails.getUsername());
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                result
        );
    }
}
