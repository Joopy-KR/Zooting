package com.zooting.api.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 검색시 응답 DTO")
public record MemberSearchRes(
        @Schema(description = "이메일")
        String email,
        @Schema(description = "닉네임")
        String nickname
) {
}
