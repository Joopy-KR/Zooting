package com.zooting.api.application.api;

import com.zooting.api.application.dto.request.MemberAndMaskReq;
import com.zooting.api.application.dto.response.MemberAndBackgroundRes;
import com.zooting.api.application.dto.response.MemberAndMaskRes;
import com.zooting.api.application.usecase.MemberAndMaskUsecase;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
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

public class MemberAndMaskController {
    private final MemberAndMaskUsecase memberAndMaskUsecase;
    @PostMapping
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
                    "구매 실패 - 잔여 포인트 부족 / 이미 보유한 배경이미지 / 유저의 동물상과 일치하지 않음"
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
