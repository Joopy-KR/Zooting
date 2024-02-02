package com.zooting.api.application.api;

import com.zooting.api.application.dto.request.MemberAndBlockReq;
import com.zooting.api.application.usecase.BlockAndFriendUsecase;
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

@RestController
@RequestMapping("/api/block")
@RequiredArgsConstructor
@Tag(name="차단과 친구", description = "차단, 친구 관련 API")
public class BlockAndFriendController {

    private final BlockAndFriendUsecase memberAndBlockAndFriendUsecase;
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping
    @Operation(
            summary = "차단 멤버 추가",
            description = "차단 멤버 추가시 친구 관계 삭제"
    )
    public ResponseEntity<BaseResponse<String>> saveBlockMember(
            @Valid @RequestBody MemberAndBlockReq blockReq,
            @AuthenticationPrincipal UserDetails userDetails) {
        memberAndBlockAndFriendUsecase.insertBlockList(userDetails.getUsername(), blockReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "멤버 차단 완료"
        );
    }
    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping
    @Operation(summary = "차단 해제")
    public ResponseEntity<BaseResponse<String>> deleteBlockMember(
            @Valid @RequestParam String nickname,
            @AuthenticationPrincipal UserDetails userDetails) {
        memberAndBlockAndFriendUsecase.deleteBlock(userDetails.getUsername(), nickname);
        return BaseResponse.success(
                SuccessCode.DELETE_SUCCESS,
                "멤버 차단 해제 완료"
        );
    }
}
