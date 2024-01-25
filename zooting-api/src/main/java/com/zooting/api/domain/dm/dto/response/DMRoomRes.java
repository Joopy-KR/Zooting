package com.zooting.api.domain.dm.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DM방 입장, 스크롤 시 응답 Dto")
public record DMRoomRes(
        @Schema(description = "DM방 ID")
        Long dmRoomId,
        @Schema(description = "발신자")
        List<DMDto> dmList,
        @Schema(description = "커서 값, DM의 ID")
        Long    cursor

) {
}
