package com.zooting.api.domain.dm.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.zooting.api.domain.file.dto.response.FileRes;

import java.time.LocalDateTime;
import java.util.List;

public record RedisDMReq(
        Long dmRoomId,
        Long dmId,
        String type,
        String message,
        String sender,
        String receiver,
        List<FileRes> files,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize
        LocalDateTime createdAt,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize
        LocalDateTime updatedAt
) {
}
