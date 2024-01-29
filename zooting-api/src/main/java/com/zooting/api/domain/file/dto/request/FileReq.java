package com.zooting.api.domain.file.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "파일 업로드 시 요청 Dto")
public record FileReq(
        @Schema(description = "파일 이름")
        String fileName,
        @Schema(description = "파일 경로")
        String imgUrl,
        @Schema(description = "폴더 경로")
        String fileDir
) {

}
