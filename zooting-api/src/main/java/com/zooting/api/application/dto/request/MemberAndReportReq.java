package com.zooting.api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "멤버 신고 요청 시 DTO")
public record MemberAndReportReq(
        @Schema(description = "신고 사유")
        String reason,
        @Schema(description = "신고 일자")
        String date,
        @Schema(description = "신고 대상 이메일")
        String email
) {
}
