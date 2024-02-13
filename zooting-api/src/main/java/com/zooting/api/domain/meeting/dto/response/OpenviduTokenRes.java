package com.zooting.api.domain.meeting.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(name = "Openvidu Token Response", description = "Openvidu 토큰 응답")
public record OpenviduTokenRes(
        @Schema(description = "Openvidu 토큰")
        String token,
        @Schema(description = "participants")
        Map<String, String> participants // key: nickname, value: gender
) {
}
