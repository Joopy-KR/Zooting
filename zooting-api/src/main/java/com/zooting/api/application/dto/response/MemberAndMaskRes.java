package com.zooting.api.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "해금마스크 조회 응답시 DTO")
public record MemberAndMaskRes(
        @Schema(description = "동물상")
        String animal,
        @Schema(description = "마스크 설명")
        String description,
        @Schema(description = "가격")
        Long price,
        @Schema(description = "파일명")
        String fileName,
        @Schema(description = "이미지 url")
        String imgUrl
) {
}
