package com.zooting.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "마스크 변경 요청시 DTO")
public record MaskReq(
        @Schema(description = "마스크 id")
        Long maskId
) {
}
