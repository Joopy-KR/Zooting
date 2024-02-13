package com.zooting.api.domain.meeting.dto.response;

import com.zooting.api.domain.meeting.dto.OppositeGenderParticipantsDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "Openvidu Token Response", description = "Openvidu 토큰 응답")
public record OpenviduTokenRes(
        @Schema(description = "Openvidu 토큰")
        String token,
        @Schema(description = "이성 리스트")
        List<OppositeGenderParticipantsDto> oppositeGenderList
) {
}
