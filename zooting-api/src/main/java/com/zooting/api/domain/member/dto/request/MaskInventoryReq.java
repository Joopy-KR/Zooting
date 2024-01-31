package com.zooting.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "마스크 변경 요청시 DTO")
public record MaskInventoryReq(
        @Schema(description = "마스크 인벤토리 id")
        Long maskInventoryId
) {
}
