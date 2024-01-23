package com.zooting.api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "배경이미지 구매 요청 DTO")
public record MemberAndBackgroundReq(
        @Schema(description = "배경이미지 id")
        @NotNull
        @Min(value = 0, message = "배경이미지 id의 최소값은 0입니다.")
        Long backgroundId
) {
}
