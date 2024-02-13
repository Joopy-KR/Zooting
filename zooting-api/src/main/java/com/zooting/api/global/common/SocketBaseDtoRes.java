package com.zooting.api.global.common;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "소켓 응답 Base DTO")
public record SocketBaseDtoRes<T>(
        SocketType type,
        T result
) {
}
