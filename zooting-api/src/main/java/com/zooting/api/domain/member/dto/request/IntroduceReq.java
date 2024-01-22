package com.zooting.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "자기소개 수정 요청 DTO")
public record IntroduceReq(
        @Schema(description = "자기소개")
        String introduce
) {
}
