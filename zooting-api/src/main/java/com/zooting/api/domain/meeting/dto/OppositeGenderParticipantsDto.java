package com.zooting.api.domain.meeting.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OppositeGenderParticipantsDto(
        @Schema(description = "닉네임")
        String nickname,
        @Schema(description = "동물상")
        String animal
) {
}
