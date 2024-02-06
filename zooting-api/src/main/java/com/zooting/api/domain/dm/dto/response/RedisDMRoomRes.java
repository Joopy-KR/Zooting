package com.zooting.api.domain.dm.dto.response;

import com.zooting.api.domain.dm.dto.request.DMReq;
import com.zooting.api.domain.dm.dto.request.RedisDMReq;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DM방 입장, 스크롤 시 응답 Dto")
public record RedisDMRoomRes(
        @Schema(description = "DM방 ID")
        Long dmRoomId,
        @Schema(description = "발신자")
        List<RedisDMReq> dmList,
        @Schema(description = "커서 값, DM의 ID")
        Long cursor

) {
}
