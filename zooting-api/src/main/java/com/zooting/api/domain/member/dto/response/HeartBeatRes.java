package com.zooting.api.domain.member.dto.response;

import java.time.LocalDateTime;

public record HeartBeatRes(
        String nickname,
        boolean status,
        LocalDateTime time
) {
}
