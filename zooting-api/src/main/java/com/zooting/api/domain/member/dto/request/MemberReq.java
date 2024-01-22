package com.zooting.api.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

@Schema(description = "추가 정보 저장 요청 DTO")
public record MemberReq(
        @Schema(description = "닉네임")
       String nickname,
        @Schema(description = "생년월일, yyyy-MM-dd")
        String birth,
       @Schema(description = "주소")
       String address,
       @Schema(description = "관심사 리스트")
       List<String> interest,
       @Schema(description = "이상형 동물 리스트")
       List<String> idealAnimal
) {
}
