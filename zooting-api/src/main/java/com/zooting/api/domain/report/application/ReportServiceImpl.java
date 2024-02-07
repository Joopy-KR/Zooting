package com.zooting.api.domain.report.application;

import com.zooting.api.domain.member.dao.MemberRepository;
import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.domain.report.dao.ReportRepository;
import com.zooting.api.domain.report.dto.ReportReq;
import com.zooting.api.domain.report.entity.ReportList;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{
    final private ReportRepository reportRepository;
    final private MemberRepository memberRepository;
    public void insertReport(ReportReq reportReq) {

        Member reportedMember = memberRepository.findMemberByNickname(reportReq.nickname())
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_USER));

        ReportList reportList = new ReportList();
        reportList.setReason(reportReq.reason());
        reportList.setMember(reportedMember);

        reportRepository.save(reportList);
    }
}
