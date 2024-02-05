package com.zooting.api.domain.notice.dao;

import com.zooting.api.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<Notice> findNoticeById(Long noticeId);
    Page<Notice> findNoticesBy(Pageable pageable);
    void deleteNoticeById(Long noticeId);
}
