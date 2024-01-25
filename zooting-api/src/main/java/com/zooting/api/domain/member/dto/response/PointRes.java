package com.zooting.api.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "포인트 조회시 응답 DTO")
public record PointRes(
        @Schema(description = "포인트")
        Long point
) {
}
