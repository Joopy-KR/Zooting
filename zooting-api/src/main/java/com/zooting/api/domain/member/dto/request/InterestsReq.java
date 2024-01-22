package com.zooting.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
@Schema(description = "관심사, 이상형 수정 요청 DTO")
public record InterestsReq(
        @Schema(description = "관심사")
        List<String> interest,
        @Schema(description = "이상형")
        List<String> idealAnimal
) {}
