package com.zooting.api.domain.friend.api;

import com.zooting.api.domain.friend.application.FriendRequestService;
import com.zooting.api.domain.friend.application.FriendService;
import com.zooting.api.domain.friend.dto.request.FriendReq;
import com.zooting.api.domain.friend.dto.response.FriendRes;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
//@PreAuthorize("hasAnyRole('USER')")
@RestController
@RequestMapping("/api/friends/request")
@RequiredArgsConstructor
@Tag(name="친구 요청", description = "친구 요청 관련 API")
public class FriendRequestController {
    private FriendRequestService friendRequestService;
    @GetMapping("")
    public void test(@AuthenticationPrincipal Authentication authentication) {
        log.info(authentication.getName());
    }
    @Operation(summary = "친구 요청 받은 리스트", description = "로그인 한 사람 기준 친구 요청 리스트 반환")
    @GetMapping("/from")
    public ResponseEntity<BaseResponse<List<FriendRes>>> getReceivedFriendRequests(@AuthenticationPrincipal Authentication authentication){
        List<FriendRes> friendResList = friendRequestService.getReceivedFriendRequests(authentication.getName());
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                friendResList
        );
    }
    @Operation(summary = "친구 요청 보낸 리스트", description = "로그인 한 사람이 보낸 친구 요청 리스트 반환")
    @GetMapping("/to")
    public ResponseEntity<BaseResponse<List<FriendRes>>> getSentFriendRequests(@AuthenticationPrincipal Authentication authentication){
        List<FriendRes> friendResList = friendRequestService.getSentFriendRequests(authentication.getName());
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                friendResList
        );
    }

    //친구 요청 보내기
    @Operation(summary = "친구 요청 보내기", description = "로그인 한 사람이 친구 요청 보내기")
    @PostMapping("")
    public ResponseEntity<BaseResponse<String>> sendFriendRequest(@Valid @NotNull @RequestBody FriendReq friendReq, @AuthenticationPrincipal Authentication authentication){
        friendRequestService.sendFriendRequest(authentication.getName(), friendReq.nickname());
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                "친구 요청 성공"
        );
    }

    //받은 친구요청 거절
    @Operation(summary = "친구 요청 거절", description = "로그인 한 사람이 친구 요청 거절")
    @DeleteMapping("reject")
    public ResponseEntity<BaseResponse<String>> rejectFriendRequest(@Valid @NotNull @RequestParam FriendReq friendReq, @AuthenticationPrincipal Authentication authentication){
        friendRequestService.rejectFriendRequest(authentication.getName(), friendReq.email());
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                "친구 요청 거절 성공"
        );
    }
    //보낸 친구요청 취소
    @Operation(summary = "친구 요청 취소", description = "로그인 한 사람이 보낸 친구 요청 취소")
    @DeleteMapping("cancel")
    public ResponseEntity<BaseResponse<String>> cancelFriendRequest(@Valid @NotNull @RequestParam FriendReq friendReq, @AuthenticationPrincipal Authentication authentication){
        friendRequestService.rejectFriendRequest(friendReq.email(), authentication.getName()); // reject 매개변수를 거꾸로 주면 요청 취소
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                "친구 요청 취소 성공"
        );
    }
}
