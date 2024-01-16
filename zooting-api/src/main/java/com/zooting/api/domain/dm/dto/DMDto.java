package com.zooting.api.domain.dm.dto;

public record DMDto(
    Long id,
    String roomId,
    String message,
    Boolean status
) {
}
