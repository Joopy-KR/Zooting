package com.zooting.api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "신고거부시 요청 DTO")
public record ReportRejectReq(
        @Schema(description = "신고목록 id")
        Long reportId
) {
}
