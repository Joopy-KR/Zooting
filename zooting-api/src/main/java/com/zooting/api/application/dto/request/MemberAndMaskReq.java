package com.zooting.api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "마스크 구매시 요청 DTO")
public record MemberAndMaskReq(
        @Schema(description = "마스크 아이디")
        Long maskId
) {
}
