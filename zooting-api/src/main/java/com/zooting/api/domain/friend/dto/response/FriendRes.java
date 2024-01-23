package com.zooting.api.domain.friend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "친구 response Dto", description = "친구 관련 response Dto")
public record FriendRes(
        @Schema(name = "이메일", description = "이메일")
        String email,
        @Schema(name = "닉네임", description = "닉네임")
        String nickname
) {

}
