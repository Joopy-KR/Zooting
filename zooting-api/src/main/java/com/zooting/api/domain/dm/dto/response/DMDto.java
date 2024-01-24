package com.zooting.api.domain.dm.dto.response;

public record DMDto(
        //TODO : file
        Long dmRoomId,
        String sender,
        String message
) {
}
