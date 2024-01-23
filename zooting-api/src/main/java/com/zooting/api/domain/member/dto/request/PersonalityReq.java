package com.zooting.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "성격 수정시 요청 DTO")
public record PersonalityReq(
        @Schema(description = "성격")
        String personality
) {
}
