package com.zooting.api.global.common;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "소켓 요청 Base dto")
public record SocketBaseDtoReq<T>(
        SocketType type,
        T request
) {

}
