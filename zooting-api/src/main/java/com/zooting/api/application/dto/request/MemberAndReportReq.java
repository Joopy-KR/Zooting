package com.zooting.api.application.dto.request;

public record MemberAndReportReq(
        String reason,
        String date,
        String email
) {
}
