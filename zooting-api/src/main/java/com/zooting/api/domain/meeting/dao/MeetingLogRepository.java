package com.zooting.api.domain.meeting.dao;

import com.zooting.api.domain.meeting.entity.MeetingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MeetingLogRepository extends JpaRepository<MeetingLog, Long> {
    List<MeetingLog> findAllByUuid(UUID uuid);
}
