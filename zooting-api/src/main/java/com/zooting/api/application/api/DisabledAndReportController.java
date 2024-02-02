package com.zooting.api.application.api;

import com.zooting.api.application.dto.request.DisabledAndReportReq;
import com.zooting.api.application.usecase.DisabledAndReportUsecase;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name="신고와 활동정지", description = "신고와 활동정지 관련 API")
public class DisabledAndReportController {
    private final DisabledAndReportUsecase disabledAndReportAndMemberUsecase;
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/disabled")
    @Operation(summary = "신고 승인",
            description = "신고 승인 시 정지기간도 보내야한다"
    )
    public ResponseEntity<BaseResponse<String>> acceptReportAndMakeMemberDisabled(
            @Valid @RequestBody DisabledAndReportReq reportReq) {
        disabledAndReportAndMemberUsecase.acceptReport(reportReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "신고 승인 완료"
        );
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping
    @Operation(summary = "신고 거부")
    public ResponseEntity<BaseResponse<String>> rejectReport(
            @Valid @RequestParam(name="reportId") Long reportId) {
        disabledAndReportAndMemberUsecase.rejectReport(reportId);
        return BaseResponse.success(
                SuccessCode.DELETE_SUCCESS,
                "신고 거부 완료"
        );
    }
}
