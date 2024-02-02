package com.zooting.api.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberStatus(
        @Schema(description = "친구 여부")
        boolean isFriend,
        @Schema(description = "차단 여부")
        boolean isBlock,
        @Schema(description = "신고 여부")
        boolean isReport
) {
}
