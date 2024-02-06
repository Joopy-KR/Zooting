package com.zooting.api.domain.notice.application;

import com.zooting.api.domain.notice.dto.request.NoticeSaveReq;
import com.zooting.api.domain.notice.dto.request.NoticeUpdateReq;

import java.util.List;

import com.zooting.api.domain.notice.dto.response.NoticePageRes;
import com.zooting.api.domain.notice.dto.response.NoticeRes;
import org.springframework.data.domain.Pageable;

public interface NoticeService {
    void saveNotice(NoticeSaveReq noticeReq);
    void updateNotice(NoticeUpdateReq noticeReq);
    NoticePageRes findNotice(Pageable pageable);
    void deleteNotice(Long noticeId);
}
