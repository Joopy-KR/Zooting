package com.zooting.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "추가 정보 수정 (지역, 이상형) 요청 DTO")
public record MemberModifyReq(
        @Schema(description = "지역")
        String address,
        @Schema(description = "이상형 동물 리스트")
        List<String> idealAnimal
) {
}
