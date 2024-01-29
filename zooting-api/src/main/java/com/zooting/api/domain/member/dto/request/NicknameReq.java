package com.zooting.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "닉네임 변경 요청시 DTO")
public record NicknameReq(
        @Schema(description = "닉네임")
        String nickname
) {
}
