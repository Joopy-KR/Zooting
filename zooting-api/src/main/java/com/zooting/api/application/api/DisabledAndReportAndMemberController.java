package com.zooting.api.application.api;

import com.zooting.api.application.dto.request.ReportRejectReq;
import com.zooting.api.application.dto.request.ReportAcceptReq;
import com.zooting.api.application.usecase.DisabledAndReportAndMemberUsecase;
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
@RequestMapping("/api/disabled")
@RequiredArgsConstructor
@Tag(name="멤버와 신고, 활동정지", description = "멤버와 신고, 활동정지 관련 API")
public class DisabledAndReportAndMemberController {
    private final DisabledAndReportAndMemberUsecase disabledAndReportAndMemberUsecase;
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/accept")
    @Operation(summary = "신고 승인",
            description = "신고 승인 시 정지기간도 보내야한다"
    )
    public ResponseEntity<BaseResponse<String>> acceptReportAndMakeMemberDisabled(
            @Valid @RequestBody ReportAcceptReq reportAcceptReq) {
        disabledAndReportAndMemberUsecase.acceptReport(reportAcceptReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "신고 승인 완료"
        );
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/reject")
    @Operation(summary = "신고 거부")
    public ResponseEntity<BaseResponse<String>> rejectReport(
            @Valid @RequestBody ReportRejectReq reportReq) {
        disabledAndReportAndMemberUsecase.rejectReport(reportReq);
        return BaseResponse.success(
                SuccessCode.DELETE_SUCCESS,
                "신고 거부 완료"
        );
    }
}
