package com.zooting.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "자기소개 수정 요청 DTO")
public record IntroduceReq(
        @Schema(description = "자기소개")
        @Size(min=10, max=2000)
        @NotNull
        String introduce
) {
}
