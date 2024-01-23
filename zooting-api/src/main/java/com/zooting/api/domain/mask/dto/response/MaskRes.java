package com.zooting.api.domain.mask.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전체마스크 조회 응답시 DTO")
public record MaskRes(
        @Schema(description = "마스크 id")
        Long maskId,
        @Schema(description = "동물상")
        String animal,
        @Schema(description = "마스크 설명, 마스크 이름?")
        String description,
        @Schema(description = "가격")
        Long price,
        @Schema(description = "이미지 파일 이름")
        String fileName,
        @Schema(description = "이미지 url")
        String imgUrl
) {
}
