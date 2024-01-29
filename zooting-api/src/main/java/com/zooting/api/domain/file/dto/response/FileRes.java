package com.zooting.api.domain.file.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "파일 업로드 시 응답 Dto")
public record FileRes(
        @Schema(description = "파일 아이디")
        UUID S3Id,
        @Schema(description = "원본 파일 이름")
        String originFileName,
        @Schema(description = "변경 파일 이름")
        String fileName,
        @Schema(description = "파일 경로")
        String imgUrl,
        @Schema(description = "폴더 경로")
        String fileDir,
        @Schema(description = "썸네일 경로")
        String thumbnailUrl
) {
}
