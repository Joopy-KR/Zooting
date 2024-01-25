package com.zooting.api.application.usecase;

import com.zooting.api.application.dto.request.ReportRejectReq;
import com.zooting.api.application.dto.request.ReportAcceptReq;
import com.zooting.api.domain.disabled.dao.DisabledRepository;
import com.zooting.api.domain.disabled.entity.DisabledMember;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.report.dao.ReportRepository;
import com.zooting.api.domain.report.entity.ReportList;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DisabledAndReportAndMemberUsecase {
    private final ReportRepository reportRepository;
    private final DisabledRepository disabledRepository;

    @Transactional
    public void acceptReport(ReportAcceptReq reportReq) {
        ReportList reportList = reportRepository.findById(reportReq.reportId())
                .orElseThrow(() -> new BaseExceptionHandler((ErrorCode.NOT_FOUND_ERROR)));

        // 신고 수락 시 신고 목록 삭제
        reportRepository.deleteById(reportReq.reportId());

        // 활동정지 유저 추가 및 기간 설정
        DisabledMember disabled = disabledRepository.findDisabledMemberByMember(reportList.getMember())
                .orElseGet(()-> DisabledMember.builder().member(reportList.getMember()).build());

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        disabled.setStartDate(sdf.format(now));

        Calendar end = Calendar.getInstance();
        end.setTime(now);
        end.add(Calendar.YEAR, reportReq.disabledYear());
        end.add(Calendar.MONTH, reportReq.disabledMonth());
        end.add(Calendar.DATE, reportReq.disabledDay());
        disabled.setEndDate(sdf.format(end.getTime()));
        disabledRepository.save(disabled);
    }
    @Transactional
    public void rejectReport(ReportRejectReq reportReq) {
        ReportList reportList = reportRepository.findById(reportReq.reportId())
                .orElseThrow(() -> new BaseExceptionHandler((ErrorCode.NOT_FOUND_USER)));
        // 신고 수락 시 신고 목록 삭제
        reportRepository.deleteById(reportReq.reportId());

    }
}
