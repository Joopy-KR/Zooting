package com.zooting.api.domain.member.dto.response;

import java.util.List;

public record AccessMemberStatus(
        List<String> onlineMemberIds,
        List<String> offlineMemberIds
) {
}
