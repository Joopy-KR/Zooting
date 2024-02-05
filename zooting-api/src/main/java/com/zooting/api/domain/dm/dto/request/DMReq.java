package com.zooting.api.domain.dm.dto.request;

import com.zooting.api.domain.file.dto.response.FileRes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "DM전송 시 요청 Dto")
public record DMReq(
        @Schema(description = "DM방 ID")
        @NotNull
        Long dmRoomId,
        @Schema(description = "Type")
        @NotNull
        Long Type, // 1이면 메세지, 2면 토큰
        @Schema(description = "메시지")
        @NotNull
        String message,
        @Schema(description = "발신자")
        @NotNull
        String sender,
        @Schema(description = "수신자")
        @NotNull
        String receiver,
        @Schema(description = "파일Id, 썸네일url")
        @Nullable
        List<FileRes> files
) {
}
