package com.zooting.api.domain.dm.dto.response;

import com.zooting.api.domain.file.dto.response.DMFileRes;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DM 리스트 Dto")
public record DMDto(
        //TODO : file
        @Schema(description = "DM방 ID")
        Long dmRoomId,
        @Schema(description = "DM ID")
        Long dmId,
        @Schema(description = "발신자")
        String sender,
        @Schema(description = "수신자")
        String message,
        @Schema(description = "파일아이디")
        List<DMFileRes> dmFiles
) {
}
