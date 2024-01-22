package com.zooting.api.domain.game.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "캐치마인드 응답 시 DTO")
public record CatchMindRes(
        @Schema(description = "랜덤 단어")
        String word
) {
}
