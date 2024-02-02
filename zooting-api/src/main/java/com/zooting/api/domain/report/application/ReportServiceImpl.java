package com.zooting.api.domain.report.application;

import com.zooting.api.domain.member.entity.Member;
import com.zooting.api.domain.report.dao.ReportRepository;
import com.zooting.api.domain.report.dto.ReportReq;
import com.zooting.api.domain.report.entity.ReportList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{
    final private ReportRepository reportRepository;
    public void insertReport(ReportReq reportReq) {
        Member reportedMember = new Member();
        reportedMember.setEmail(reportReq.email());

        ReportList reportList = new ReportList();
        reportList.setReason(reportReq.reason());
        reportList.setMember(reportedMember);

        reportRepository.save(reportList);
    }
}
