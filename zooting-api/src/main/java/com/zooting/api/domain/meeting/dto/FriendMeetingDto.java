package com.zooting.api.domain.meeting.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "친구 미팅 신청 시 Response", description = "친구 미팅 신청 시 응답")
public record FriendMeetingDto(

        @Schema(description = "Websocket Message Type")
        String type,
        @Schema(description = "요청한 사람 이메일")
        String email,
        @Schema(description = "요청한 사람 닉네임")
        String nickname
) {
}
