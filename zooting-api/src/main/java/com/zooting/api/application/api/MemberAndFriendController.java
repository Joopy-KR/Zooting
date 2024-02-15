package com.zooting.api.application.api;

import com.zooting.api.application.usecase.MemberAndFriendAndFriendRequestUsecase;
import com.zooting.api.application.usecase.MemberAndFriendRequestUsecase;
import com.zooting.api.domain.friend.dto.request.FriendReq;
import com.zooting.api.domain.member.application.MemberHeartbeatService;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import com.zooting.api.global.security.userdetails.CustomUserDetails;
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
    private final MemberHeartbeatService memberHeartbeatService;

    //친구 요청 보내기
    @Operation(summary = "친구 요청 보내기", description = "로그인 한 사람이 친구 요청 보내기")
    @PostMapping("")
    public ResponseEntity<BaseResponse<String>> sendFriendRequest(@Valid @NotNull @RequestBody FriendReq friendReq, @AuthenticationPrincipal UserDetails userDetails) {
        memberAndFriendRequestUsecase.sendFriendRequest(userDetails.getUsername(), friendReq.nickname());
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                "친구 요청 성공"
        );
    }

    @Operation(summary = "친구 수락", description = "로그인 한 사람 기준 요청 온 친구 수락")
    @PostMapping("/accept")
    public ResponseEntity<BaseResponse<String>> acceptFriend(@Valid @NotNull @RequestBody FriendReq friendReq, @AuthenticationPrincipal UserDetails userDetails) {
        String followerEmail = memberAndFriendAndFriendRequestUsecase.acceptFriend(userDetails.getUsername(), friendReq.nickname());
        // 온라인 상태라면 친구에 변화가 생각다는 것을 알 수 있도록 등록
        memberHeartbeatService.alertFriendUpdate(userDetails.getUsername(), followerEmail);
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                "친구 수락 성공"
        );
    }

    @Operation(summary = "친구 삭제", description = "로그인 한 사람 기준 친구 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse<String>> deleteFriend(@Valid @NotNull @RequestParam String nickname, @AuthenticationPrincipal UserDetails userDetails) {
        String followerEmail = memberAndFriendAndFriendRequestUsecase.deleteFriend(userDetails.getUsername(), nickname);
        // 온라인 상태라면 친구에 변화가 생각다는 것을 알 수 있도록 등록
        memberHeartbeatService.alertFriendUpdate(userDetails.getUsername(), followerEmail);
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                "친구 삭제 성공"
        );
    }

    //받은 친구요청 거절
    @Operation(summary = "친구 요청 거절", description = "로그인 한 사람이 친구 요청 거절")
    @DeleteMapping("reject")
    public ResponseEntity<BaseResponse<String>> rejectFriendRequest(@Valid @NotNull @RequestParam String nickname, @AuthenticationPrincipal UserDetails userDetails) {
        memberAndFriendAndFriendRequestUsecase.rejectFriendRequest(userDetails.getUsername(), nickname);
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                "친구 요청 거절 성공"
        );
    }
    //보낸 친구요청 취소

    @Operation(summary = "친구 요청 취소", description = "로그인 한 사람이 보낸 친구 요청 취소")
    @DeleteMapping("cancel")
    public ResponseEntity<BaseResponse<String>> cancelFriendRequest(@Valid @NotNull @RequestParam String nickname, @AuthenticationPrincipal UserDetails userDetails) {
        memberAndFriendAndFriendRequestUsecase.cancelFriendRequest(userDetails.getUsername(), nickname);
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                "친구 요청 취소 성공"
        );
    }
}
