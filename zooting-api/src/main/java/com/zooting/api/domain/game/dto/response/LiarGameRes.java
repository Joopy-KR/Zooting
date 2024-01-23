package com.zooting.api.domain.game.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "라이어게임 응답시 DTO")
public record LiarGameRes(
        @Schema(description = "게임 주제")
        String topic,
        @Schema(description = "라이어가 보는 단어")
        String liarWord,
        @Schema(description = "나머지 유저가 보는 단어")
        String word
) {
}
