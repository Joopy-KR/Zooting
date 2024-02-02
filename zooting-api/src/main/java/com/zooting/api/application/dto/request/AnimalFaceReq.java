package com.zooting.api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "동물상 저장 요청시 DTO")
public record AnimalFaceReq(
        @Schema(description = "동물상")
        String animal,
        @Schema(description = "동물상을 닮은 비율")
        Long percentage
) {
}
