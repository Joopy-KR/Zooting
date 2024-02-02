package com.zooting.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "배경 이미지 변경 요청시 DTO")
public record BackgroundReq(
        @Schema(description = "배경 이미지 id")
        Long backgroundId
) {
}
