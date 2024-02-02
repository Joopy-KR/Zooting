package com.zooting.api.domain.report.application;

import com.zooting.api.domain.report.dto.ReportReq;

public interface ReportService {
    void insertReport(ReportReq reportReq);
}
