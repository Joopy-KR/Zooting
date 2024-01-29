package com.zooting.api.application.api;

import com.zooting.api.domain.friend.dto.request.FriendReq;
import com.zooting.api.application.usecase.MemberAndFriendAndFriendRequestUsecase;
import com.zooting.api.application.usecase.MemberAndFriendRequestUsecase;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Log4j2
@PreAuthorize("hasAnyRole('USER')")
@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
@Tag(name = "멤버 & 친구", description = "친구 요청 및 수락 컨트롤러")
public class MemberAndFriendController {

    private final MemberAndFriendAndFriendRequestUsecase memberAndFriendAndFriendRequestUsecase;
    private final MemberAndFriendRequestUsecase memberAndFriendRequestUsecase;

    //친구 요청 보내기
    @Operation(summary = "친구 요청 보내기", description = "로그인 한 사람이 친구 요청 보내기")
    @PostMapping("")
    public ResponseEntity<BaseResponse<String>> sendFriendRequest(@Valid @NotNull @RequestBody FriendReq friendReq, @AuthenticationPrincipal UserDetails userDetails){
        memberAndFriendRequestUsecase.sendFriendRequest(userDetails.getUsername(), friendReq.email());
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                "친구 요청 성공"
        );
    }

    @Operation(summary = "친구 수락", description = "로그인 한 사람 기준 요청 온 친구 수락")
    @PostMapping("/accept")
    public ResponseEntity<BaseResponse<String>> acceptFriend(@Valid @NotNull @RequestBody FriendReq friendReq, @AuthenticationPrincipal UserDetails userDetails){
        memberAndFriendAndFriendRequestUsecase.acceptFriend(friendReq, userDetails);
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                "친구 수락 성공"
        );
    }

    @Operation(summary = "친구 삭제", description = "로그인 한 사람 기준 친구 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse<String>> deleteFriend(@Valid @NotNull @RequestBody FriendReq friendReq, @AuthenticationPrincipal UserDetails userDetails){
        memberAndFriendAndFriendRequestUsecase.deleteFriend(userDetails.getUsername(), friendReq.email());
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                "친구 삭제 성공"
        );
    }
}
