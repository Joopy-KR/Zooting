package com.zooting.api.domain.member.dto.response;

import com.zooting.api.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "유저 검색시 응답 DTO")
public record MemberRes(
        @Schema(description = "이메일")
        String email,
        @Schema(description = "닉네임")
        String nickname
) {
}
