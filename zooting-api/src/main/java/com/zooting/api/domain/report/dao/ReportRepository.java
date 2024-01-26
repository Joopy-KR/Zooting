package com.zooting.api.domain.report.dao;

import com.zooting.api.domain.report.entity.ReportList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<ReportList, Long> {
}
