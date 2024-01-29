package com.zooting.api.domain.notice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공지사항 삭제시 요청 DTO")
public record NoticeDeleteReq(
        @Schema(description = "글 id")
        Long noticeId
) {
}
