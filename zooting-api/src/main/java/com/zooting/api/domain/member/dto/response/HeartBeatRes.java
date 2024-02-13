package com.zooting.api.domain.member.dto.response;

import java.util.List;

public record HeartBeatRes(
        List<String> onlineFriends
) {
}
