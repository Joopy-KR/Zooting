package com.zooting.api.application.usecase;

import com.zooting.api.application.dto.request.MemberAndReportReq;
import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.domain.report.dao.ReportRepository;
import com.zooting.api.domain.report.entity.ReportList;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberAndReportUsecase {
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    @Transactional
    public void insertReport(String userId, MemberAndReportReq reportReq) {
        Member reportedMember = memberRepository.findMemberByEmail(userId)
                .orElseThrow(() -> new BaseExceptionHandler((ErrorCode.NOT_FOUND_USER)));
        ReportList reportList = new ReportList();
        reportList.setReason(reportReq.reason());
        reportList.setDate(reportReq.date());
        reportList.setMember(reportedMember);

        reportRepository.save(reportList);
    }
}
