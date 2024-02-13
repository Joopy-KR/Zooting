package com.zooting.api.domain.meeting.dao;

import com.zooting.api.domain.meeting.entity.MeetingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingLogRepository extends JpaRepository<MeetingLog, Long> {
}
