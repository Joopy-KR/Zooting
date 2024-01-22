package com.zooting.api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "배경이미지 구매 요청 DTO")
public record MemberAndBackgroundReq(
        @Schema(description = "배경이미지 id")
        Long backgroundId
) {
}
