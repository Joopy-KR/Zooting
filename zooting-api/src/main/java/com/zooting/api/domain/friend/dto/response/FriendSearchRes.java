package com.zooting.api.domain.friend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "친구 검색 응답시 dto")
public record FriendSearchRes(
        @Schema(description = "닉네임")
        String nickname,
        @Schema(description = "gender")
        String gender
) {
}
