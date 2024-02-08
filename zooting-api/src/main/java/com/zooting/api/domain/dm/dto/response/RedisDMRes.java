package com.zooting.api.domain.dm.dto.response;

import com.zooting.api.domain.file.dto.response.DMFileRes;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Redis DM 시 요답 Dto")
public record RedisDMRes(
        Long dmRoomId,
        Long dmId,
        String type,
        String message,
        String sender,
        String receiver,
        List<DMFileRes> files
) {
}
