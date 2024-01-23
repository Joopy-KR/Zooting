package com.zooting.api.domain.game.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "밸런스게임 응답 시 DTO")
public record BalanceGameRes(
        @Schema(description = "문장1")
        String sentence1,
        @Schema(description = "문장2")
        String sentence2

) {
}
