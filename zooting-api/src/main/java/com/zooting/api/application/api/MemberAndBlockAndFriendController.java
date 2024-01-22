package com.zooting.api.application.api;

import com.zooting.api.application.dto.request.MemberAndBlockReq;
import com.zooting.api.application.usecase.MemberAndBlockAndFriendUsecase;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/block")
@RequiredArgsConstructor
public class MemberAndBlockAndFriendController {

    private final MemberAndBlockAndFriendUsecase memberAndBlockAndFriendUsecase;
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping
    public ResponseEntity<BaseResponse<String>> saveBlockMember(
            @RequestBody MemberAndBlockReq insertBlockListReq,
            @AuthenticationPrincipal UserDetails userDetails) {
        memberAndBlockAndFriendUsecase.insertBlockList(userDetails.getUsername(), insertBlockListReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "멤버 차단 완료"
        );
    }
    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping
    public ResponseEntity<BaseResponse<String>> deleteBlockMember(
            @RequestBody MemberAndBlockReq blockReq,
            @AuthenticationPrincipal UserDetails userDetails) {
        memberAndBlockAndFriendUsecase.deleteBlock(userDetails.getUsername(), blockReq);
        return BaseResponse.success(
                SuccessCode.DELETE_SUCCESS,
                "멤버 차단 해제 완료"
        );
    }
}
