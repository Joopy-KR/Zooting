package com.zooting.api.domain.report.dao;

import com.zooting.api.domain.report.entity.ReportList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportList, Long> {
}
