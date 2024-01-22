package com.zooting.api.domain.member.dto.response;

import com.zooting.api.domain.member.entity.Member;

import java.util.List;

public record MemberRes(
        String email,
        String nickname
) {
}
