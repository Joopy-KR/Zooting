package com.zooting.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "배경 이미지 변경 요청시 DTO")
public record BackgroundInventoryReq(
        @Schema(description = "배경 이미지 인벤토리 id")
        Long backgroundInventoryId
) {
}
