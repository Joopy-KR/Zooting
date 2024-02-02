package com.zooting.api.domain.notice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공지사항 조회 응답 시 DTO")
public record NoticeRes(
        @Schema(description = "글 id")
        Long noticeId,
        @Schema(description = "글 제목")
        String title,
        @Schema(description = "글 내용")
        String content
//        @Schema(description = "작성일")
//        String createdAt
) {
}
