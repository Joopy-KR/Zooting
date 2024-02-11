package com.zooting.api.domain.meeting.api;

import com.zooting.api.domain.meeting.application.MeetingService;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meeting")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/register")
    @Operation(summary = "미팅 대기방 등록", description = "미팅 대기방 등록")
    public ResponseEntity<BaseResponse<String>> registerToWaitingRoom(
            @AuthenticationPrincipal UserDetails userDetails) {
        return BaseResponse.success(SuccessCode.CHECK_SUCCESS, meetingService.registerToWaitingRoom(userDetails));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("/exit")
    @Operation(summary = "미팅 대기방 등록 취소", description = "미팅 대기방 등록 취소")
    public ResponseEntity<BaseResponse<String>> exitFromWaitingRoom(
            @AuthenticationPrincipal UserDetails userDetails, @RequestParam(name = "room") String waitingRoomId) {
        meetingService.exitFromWaitingRoom(userDetails, waitingRoomId);
        return BaseResponse.success(SuccessCode.CHECK_SUCCESS, "미팅 대기방 등록 취소에 성공했습니다.");
    }
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/accept")
    @Operation(summary = "매칭 수락", description = "매칭 수락")
    public ResponseEntity<BaseResponse<String>> acceptMatching(
            @RequestParam(name = "room") String waitingRoomId) {
        meetingService.acceptMatching(waitingRoomId);
        return BaseResponse.success(SuccessCode.CHECK_SUCCESS, "미팅을 수락했습니다.");
    }
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/request/friend")
    @Operation(summary = "친구 목록 기반 1대1 미팅 신청", description = "1대1 미팅 신청")
    public ResponseEntity<BaseResponse<String>> requestMeeting(@RequestParam String nickname, @AuthenticationPrincipal UserDetails userDetails) {
        meetingService.requestMeeting(nickname, userDetails.getUsername());
        return BaseResponse.success(SuccessCode.CHECK_SUCCESS, "미팅 신청에 성공했습니다.");
    }
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/accept/friend")
    @Operation(summary = "1대1 매칭 수락", description = "1대1 매칭 수락")
    public ResponseEntity<BaseResponse<String>> acceptFriendMeeting(@RequestParam String nickname, @AuthenticationPrincipal UserDetails userDetails) {
        meetingService.sendOpenViduTokenToClient(nickname, userDetails.getUsername());
        return BaseResponse.success(SuccessCode.CHECK_SUCCESS, "미팅을 수락했습니다.");
    }
}