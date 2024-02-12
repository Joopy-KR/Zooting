package com.zooting.api.domain.meeting.pubsub;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Openvidu Token Response", description = "Openvidu 토큰 응답")
public record OpenviduTokenRes(
        @Schema(description = "Openvidu 토큰")
        String token

) {
}
