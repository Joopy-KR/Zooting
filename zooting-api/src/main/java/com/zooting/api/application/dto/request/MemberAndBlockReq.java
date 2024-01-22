package com.zooting.api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "차단 추가, 해제 시 요청 DTO")
public record MemberAndBlockReq(
        @Schema(description = " 차단 대상 닉네임")
        String nickname
) {
}
