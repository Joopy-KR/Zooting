package com.zooting.api.domain.member.dto.response;

import com.zooting.api.domain.mask.dto.response.MaskRes;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "멤버 검색결과 요청시 페이지 정보를 담은 응답 DTO")
public record MemberSearchPageRes(
        @Schema(description = "닉네임 리스트")
        List<MemberSearchRes> searchResList,
        @Schema(description = "현재 페이지")
        int currentPage,
        @Schema(description = "전체 페이지")
        int totalPage
) {
}
