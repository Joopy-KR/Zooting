package com.zooting.api.domain.file.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DM불러올 때 파일 응답 Dto")
public record DMFileRes(
        @Schema(description = "파일 아이디")
        Long fileId,
        @Schema(description = "썸네일 경로")
        String thumbnailUrl
) {
}
