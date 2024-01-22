package com.zooting.api.application.api;

import com.zooting.api.application.dto.request.MemberAndReportReq;
import com.zooting.api.application.usecase.MemberAndReportUsecase;
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
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name="멤버와 신고", description = "멤버와 신고 관련 API")
public class MemberAndReportController {
    private final MemberAndReportUsecase memberAndReportUsecase;

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping
    @Operation(summary = "멤버 신고")
    public ResponseEntity<BaseResponse<String>> insertReport(
            @Valid @RequestBody MemberAndReportReq reportReq,
            @AuthenticationPrincipal UserDetails userDetails) {
        memberAndReportUsecase.insertReport(userDetails.getUsername(), reportReq);
        return BaseResponse.success(
                SuccessCode.INSERT_SUCCESS,
                reportReq.email() + "에 대한 신고 완료"
        );
    }
}
