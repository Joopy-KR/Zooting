package com.zooting.api.domain.dm.dto.response;

import com.zooting.api.domain.file.dto.response.DMFileRes;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Schema(description = "Redis DM 시 요답 Dto")
public record RedisDMRes(
        @Schema(description = "DM방 ID")
        Long dmRoomId,
        @Schema(description = "DM ID")
        Long dmId,
        @Schema(description = "메시지 타입")
        String type,
        @Schema(description = "DM 내용")
        String message,
        @Schema(description = "발신자")
        String sender,
        @Schema(description = "수신자")
        String receiver,
        @Schema(description = "파일 리스트")
        List<DMFileRes> files,
        @Schema(description = "생성일")
        String createdAt
) {
}
