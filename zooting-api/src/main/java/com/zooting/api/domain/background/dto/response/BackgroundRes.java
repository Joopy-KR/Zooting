package com.zooting.api.domain.background.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전체배경 조회시 응답 DTO")
public record BackgroundRes(
        @Schema(description = "배경 id")
        Long backgroundId,

        @Schema(description = "파일이름")
        String fileName,
        @Schema(description = "이미지 url")
        String imgUrl,
        @Schema(description = "배경 이미지 가격")
        Long price
) {
}
