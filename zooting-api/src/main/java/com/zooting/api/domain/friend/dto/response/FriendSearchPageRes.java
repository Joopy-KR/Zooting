package com.zooting.api.domain.friend.dto.response;

import com.zooting.api.domain.member.dto.response.MemberSearchRes;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "친구 검색 응답시 dto")
public record FriendSearchPageRes(
        @Schema(description = "친구 리스트")
        List<FriendSearchRes> searchResList,
        @Schema(description = "현재 페이지")
        int currentPage,
        @Schema(description = "전체 페이지")
        int totalPage
) {
}
