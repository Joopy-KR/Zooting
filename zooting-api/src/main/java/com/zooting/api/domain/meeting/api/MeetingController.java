package com.zooting.api.domain.meeting.api;

import com.zooting.api.domain.meeting.application.MeetingService;
import com.zooting.api.domain.meeting.dto.FriendMeetingDto;
import com.zooting.api.domain.meeting.dto.MeetingPickDto;
import com.zooting.api.domain.meeting.dto.response.MeetingMemberRes;
import com.zooting.api.domain.meeting.dto.response.OpenviduTokenRes;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.SocketBaseDtoRes;
import com.zooting.api.global.common.SocketType;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/meeting")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;
    private final SimpMessageSendingOperations webSocketTemplate;


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
    @GetMapping("/refresh")
    @Operation(summary = "접속이 끊긴 유저에게 Openvidu Token 재발급",
            description = "접속이 끊긴 유저에게 Openvidu Token 재발급")
    public ResponseEntity<BaseResponse<OpenviduTokenRes>> refreshOpenviduToken(
            @RequestParam(name = "sessionId") String sessionId) {
        OpenviduTokenRes openviduTokenRes = meetingService.refreshOpenviduToken(sessionId);
        return BaseResponse.success(SuccessCode.CHECK_SUCCESS, openviduTokenRes);
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
        Map<String, OpenviduTokenRes> openviduTokenResMap = meetingService.sendOpenViduTokenToClient(nickname, userDetails.getUsername());
        for (Map.Entry<String, OpenviduTokenRes> entry : openviduTokenResMap.entrySet()) {
            webSocketTemplate.convertAndSend("/api/sub/" + entry.getKey(), new SocketBaseDtoRes<>(SocketType.OPENVIDU, entry.getValue()));
        }
        return BaseResponse.success(SuccessCode.CHECK_SUCCESS, "미팅을 수락했습니다.");
    }

    /* 간단한 버전 */
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/pick")
    @Operation(summary = "화상채팅 종료 시 사람 선택", description = "화상채팅 종료 시 사람 선택")
    public ResponseEntity<BaseResponse<String>> pickPerson(@RequestParam String nickname, @AuthenticationPrincipal UserDetails userDetails) {
        Map.Entry<String, MeetingPickDto> entry = meetingService.pickPerson(nickname, userDetails.getUsername()).entrySet().iterator().next();
        webSocketTemplate.convertAndSend("/api/sub/" + entry.getKey(), new SocketBaseDtoRes<>(SocketType.PICK, entry.getValue()));
        return BaseResponse.success(SuccessCode.CHECK_SUCCESS, nickname + "을 선택했습니다.");
    }

    /* 복잡한 버전 */
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/picks")
    @Operation(summary = "화상채팅 종료 시 사람 선택2", description = "화상채팅 종료 시 사람 선택2")
    public ResponseEntity<BaseResponse<String>> picksPerson(@RequestParam String sessionId, @RequestParam String nickname, @AuthenticationPrincipal UserDetails userDetails) {
        meetingService.picksPerson(sessionId, nickname, userDetails.getUsername());
        return BaseResponse.success(SuccessCode.CHECK_SUCCESS, nickname + "을 선택했습니다.");
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/picks/result")
    @Operation(summary = "선택 결과 보기", description = "화상채팅 종료 시 사람 선택2")
    public ResponseEntity<BaseResponse<List<MeetingPickDto>>> showResult(@RequestParam String sessionId, @AuthenticationPrincipal UserDetails userDetails) {
        List<MeetingPickDto> meetingPickDtos = meetingService.showResult(sessionId, userDetails.getUsername());
        return BaseResponse.success(SuccessCode.CHECK_SUCCESS, meetingPickDtos);
    }

    /* 1대1 미팅 거절 */
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/reject/friend")
    @Operation(summary = "친구 목록 기반 1대1 미팅 거절", description = "1대1 미팅 거절")
    public ResponseEntity<BaseResponse<String>> rejectMeeting(@RequestParam String nickname, @AuthenticationPrincipal UserDetails userDetails) {
        Map.Entry<String, FriendMeetingDto> entry = meetingService.rejectMeeting(nickname, userDetails.getUsername()).entrySet().iterator().next();
        webSocketTemplate.convertAndSend("/api/sub/" + entry.getKey(), new SocketBaseDtoRes<>(SocketType.REJECT, entry.getValue()));
        return BaseResponse.success(SuccessCode.CHECK_SUCCESS, "미팅 거절에 성공했습니다.");
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/log")
    @Operation(summary = "유저의 최근 미팅 목록 가져오기", description = "유저의 최근 미팅 목록 가져오기")
    public ResponseEntity<BaseResponse<List<MeetingMemberRes>>> findRecentMeetingRoomMembers(@AuthenticationPrincipal UserDetails userDetails){
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                meetingService.findRecentMeetingMembers(userDetails));
    }
}