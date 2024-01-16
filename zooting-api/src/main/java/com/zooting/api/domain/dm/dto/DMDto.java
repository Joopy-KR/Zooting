package com.zooting.api.domain.dm.dto;

public record DMDto(
    Long id,
    Long room_id,
    String message,
    Boolean status
) {
}
