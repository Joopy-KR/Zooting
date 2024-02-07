package com.zooting.api.domain.meeting.api;

import com.zooting.api.domain.meeting.application.MeetingService;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping("/register")
    @Operation(summary = "미팅 대기방 등록", description = "미팅 대기방 등록")
    public ResponseEntity<BaseResponse<String>> registerToWaitingRoom(
            @AuthenticationPrincipal UserDetails userDetails) {
        return BaseResponse.success(SuccessCode.CHECK_SUCCESS, meetingService.registerToWaitingRoom(userDetails));
    }

    @DeleteMapping("/exit")
    @Operation(summary = "미팅 대기방 등록 취소", description = "미팅 대기방 등록 취소")
    public ResponseEntity<BaseResponse<String>> exitFromWaitingRoom(
            @AuthenticationPrincipal UserDetails userDetails, @RequestParam(name = "room") String waitingRoomId) {
        meetingService.exitFromWaitingRoom(userDetails, waitingRoomId);
        return BaseResponse.success(SuccessCode.CHECK_SUCCESS, "미팅 대기방 등록 취소에 성공했습니다.");
    }
}