package com.zooting.api.domain.dm.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DM 리스트 Dto")
public record DMDto(
        //TODO : file
        @Schema(description = "DM방 ID")
        Long dmRoomId,
        @Schema(description = "발신자")
        String sender,
        @Schema(description = "수신자")
        String message
) {
}
