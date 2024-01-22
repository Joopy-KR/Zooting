package com.zooting.api.domain.animalface.dto.request;

import com.zooting.api.domain.member.entity.Member;

public record AnimalFaceReq(
        Member member,
        Long animal1,
        Long animal2,
        Long animal3,
        Long animal4,
        Long animal5
) {
}
