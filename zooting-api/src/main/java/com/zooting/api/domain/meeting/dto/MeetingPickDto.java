package com.zooting.api.domain.meeting.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record MeetingPickDto(
        @Schema(description = "선택하는 사람 닉네임")
        String nickname,
        @Schema(description = "선택받는 사람 닉네임")
        String selectedNickname
) {
}
