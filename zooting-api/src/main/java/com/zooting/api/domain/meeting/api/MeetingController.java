package com.zooting.api.domain.meeting.api;

import com.zooting.api.domain.meeting.application.MeetingService;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meeting")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;
    @PostMapping("/register")
    @Operation(summary = "대기방 조회 및 등록", description = "대기방 조회 및 등록")
    public ResponseEntity<BaseResponse<String>> registerToWaitingRoom(
            @AuthenticationPrincipal UserDetails userDetails) {
        String sessionId = meetingService.registerToWaitingRoom(userDetails);
        return BaseResponse.success(
                SuccessCode.CHECK_SUCCESS,
                sessionId
        );
    }
}
