package com.zooting.api.domain.report.api;

import com.zooting.api.domain.report.application.ReportService;
import com.zooting.api.domain.report.dto.ReportReq;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/reports")
@RequiredArgsConstructor
@Tag(name="신고", description = "신고 관련 API")

public class ReportController {
    final private ReportService reportService;
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping
    @Operation(summary = "멤버 신고")
    public ResponseEntity<BaseResponse<String>> insertReport(
            @Valid @RequestBody ReportReq reportReq,
            @AuthenticationPrincipal UserDetails userDetails) {
        reportService.insertReport(reportReq);
        return BaseResponse.success(
                SuccessCode.INSERT_SUCCESS,
                reportReq.nickname() + "에 대한 신고 완료"
        );
    }
}
