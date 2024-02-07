package com.zooting.api.domain.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "멤버 신고 요청 시 DTO")
public record ReportReq(
        @Size(min=1, max=2000, message = "신고 사유는 1에서 2000자 사이입니다.")
        @Schema(description = "신고 사유")
        String reason,
        @Schema(description = "신고 대상 닉네임")
        @NotNull
        String nickname
) {
}
