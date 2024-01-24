package com.zooting.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;
@Schema(description = "관심사, 이상형 수정 요청 DTO")
public record InterestsReq(
        @Schema(description = "관심사")
        @NotNull
        List<String> interest,
        @Schema(description = "이상형")
        @NotNull
        List<String> idealAnimal
) {}
