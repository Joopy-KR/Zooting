package com.zooting.api.domain.mask.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전체마스크 조회 시, 해금마스크 조회 시 응답 DTO")
public record MaskRes(
        @Schema(description = "마스크 id")
        Long maskId,
        @Schema(description = "동물상")
        String animal,
        @Schema(description = "마스크 설명, 마스크 이름?")
        String description,
        @Schema(description = "가격")
        Long price,
        @Schema(description = "파일 폴더")
        String fileDir,
        @Schema(description = "이미지 파일 이름")
        String fileName,
        @Schema(description = "이미지 url")
        String imgUrl,
        @Schema(description = "썸네일 url")
        String thumbnailUrl
) {
}
