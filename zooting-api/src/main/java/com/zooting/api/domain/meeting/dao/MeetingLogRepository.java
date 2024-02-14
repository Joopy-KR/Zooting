package com.zooting.api.domain.meeting.dao;

import com.zooting.api.domain.meeting.entity.MeetingLog;
import com.zooting.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MeetingLogRepository extends JpaRepository<MeetingLog, Long> {
    List<MeetingLog> findAllByUuid(UUID uuid);

    @Query("""
            SELECT ml.member 
            FROM MeetingLog ml 
            WHERE ml.uuid 
            IN 
            (SELECT DISTINCT(ml2.uuid) 
            FROM MeetingLog ml2 
            WHERE ml2.member.email = :email 
            ORDER BY ml2.createdAt DESC)
            """)
    List<Member> findMembersByUuidAssociatedWithEmail(String email);
}
