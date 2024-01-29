package com.zooting.api.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 검색시, 차단 멤버 조회시 응답 DTO")
public record MemberSearchRes(
        @Schema(description = "이메일")
        String email,
        @Schema(description = "닉네임")
        String nickname,
        @Schema(description = "성별")
        String gender,
        @Schema(description = "동물상")
        String animal
) {
}
