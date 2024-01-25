package com.zooting.api.domain.notice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "공지사항 수정 요청시 DTO")
public record NoticeUpdateReq(
        @Schema(description = "글 id")
        Long noticeId,
        @Schema(description = "글 제목")
        String title,
        @Schema(description = "글 내용")
        String content
) {
}
