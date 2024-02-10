package com.zooting.api.domain.file.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "DM불러올 때 파일 응답 Dto")
public record DMFileRes(
        @Schema(description = "파일 S3아이디")
        UUID S3Id,
        @Schema(description = "파일 경로")
        String imgUrl,
        @Schema(description = "썸네일 경로")
        String thumbnailUrl
) {
}
