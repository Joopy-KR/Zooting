package com.zooting.api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "신고승인 요청시 DTO")
public record DisabledAndReportReq(
        @Schema(description = "신고목록 id")
        Long reportId,
        @Schema(description = "활동정지 기간 년수")
        int disabledYear,
        @Schema(description = "활동정지 기간 달수")
        int disabledMonth,
        @Schema(description = "활동정지 기간 일수")
        int disabledDay
) {
}
