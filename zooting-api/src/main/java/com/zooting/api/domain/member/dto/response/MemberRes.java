package com.zooting.api.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(description = "회원 정보 조회시 응답 DTO")
public record MemberRes(
        @Schema(description = "이메일")
        String email,
        @Schema(description = "성별")
        String gender,
        @Schema(description = "닉네임")
        String nickname,
        @Schema(description = "생년월일, yyyy-MM-dd")
        Date birth,
        @Schema(description = "주소")
        String address,
        @Schema(description = "포인트")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long point,
        @Schema
        String introduce,
        @Schema(description = "성격")
        String personality,
        @Schema(description = "동물상")
        String animal,
        @Schema(description = "관심사")
        String interest,
        @Schema(description = "이상형")
        String idealAnimal,
        @Schema(description = "배경이미지 id")
        Long backgroundId,
        @Schema(description = "배경이미지 이미지 url")
        String backgroundImgUrl,
        @Schema(description = "마스크 id")
        Long maskId,
        @Schema(description = "마스크 이미지 url")
        String maskImgUrl,
        @Schema(description = "멤버 상태 관리")
        MemberStatus memberStatus
) {
}
