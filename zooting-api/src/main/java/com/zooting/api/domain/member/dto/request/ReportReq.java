package com.zooting.api.domain.member.dto.request;

public record ReportReq(
        String reason,
        String date,
        String email
) {
}
