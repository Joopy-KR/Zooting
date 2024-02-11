package com.zooting.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "포인트 추가 요청시 DTO")
public record PointsReq(
        @Schema(description = "추가할 포인트")
        @NotNull
        Long points
) {
}
