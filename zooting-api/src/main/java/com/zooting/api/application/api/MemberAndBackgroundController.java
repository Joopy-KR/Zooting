package com.zooting.api.application.api;

import com.zooting.api.application.dto.request.MemberAndBackgroundReq;
import com.zooting.api.application.dto.response.MemberAndBackgroundRes;
import com.zooting.api.application.usecase.MemberAndBackgroundUsecase;
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
@RequestMapping("/api/background")
@RequiredArgsConstructor
public class MemberAndBackgroundController {
    private final MemberAndBackgroundUsecase memberAndBackgroundUsecase;
    @PostMapping
    public ResponseEntity<BaseResponse<String>> buyBackgroundImg(
            @RequestBody MemberAndBackgroundReq backgroundReq,
            @AuthenticationPrincipal UserDetails userDetails) {

        Boolean buyBGImg = memberAndBackgroundUsecase.buyBackgroundImg(userDetails.getUsername(), backgroundReq);
        if (buyBGImg) {
            return BaseResponse.success(
                    SuccessCode.UPDATE_SUCCESS,
                    "구매 완료"
            );
        }else {
            return BaseResponse.success(
                    SuccessCode.UPDATE_SUCCESS,
                    "구매 실패 - 잔여 포인트 부족 혹은 이미 보유한 배경이미지"
            );
        }
    }
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
