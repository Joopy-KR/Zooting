package com.zooting.api.domain.file.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "파일 업로드 시 응답 Dto")
public record FileRes(
        UUID S3Id,
        String originFileName,
        String fileName,
        String imgUrl
) {
}
