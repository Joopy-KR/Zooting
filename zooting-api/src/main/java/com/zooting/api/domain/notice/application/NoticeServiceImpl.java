package com.zooting.api.domain.notice.application;

import com.zooting.api.domain.notice.dao.NoticeRepository;
import com.zooting.api.domain.notice.dto.request.NoticeDeleteReq;
import com.zooting.api.domain.notice.dto.request.NoticeSaveReq;
import com.zooting.api.domain.notice.dto.request.NoticeUpdateReq;
import com.zooting.api.domain.notice.dto.response.NoticeRes;
import com.zooting.api.domain.notice.entity.Notice;
import com.zooting.api.global.common.code.ErrorCode;
import com.zooting.api.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{
    private final NoticeRepository noticeRepository;
    @Override
    public void saveNotice(NoticeSaveReq noticeReq) {
        Notice notice = new Notice();
        notice.setTitle(noticeReq.title());
        notice.setContent(noticeReq.content());
        noticeRepository.save(notice);
    }

    @Override
    public void updateNotice(NoticeUpdateReq noticeReq) {
        Notice notice = noticeRepository.findNoticeById(noticeReq.noticeId())
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
        notice.setTitle(noticeReq.title());
        notice.setContent(noticeReq.content());
        noticeRepository.save(notice);
    }

    @Override
    public List<NoticeRes> findNotice(Pageable pageable) {
        List<Notice> noticeList = noticeRepository.findNoticesBy(pageable).getContent();
        return noticeList.stream().map(ntc -> new NoticeRes(ntc.getId(), ntc.getTitle(), ntc.getContent())).toList();
    }

    @Override
    @Transactional
    public void deleteNotice(NoticeDeleteReq noticeDeleteReq) {
        noticeRepository.deleteNoticeById(noticeDeleteReq.noticeId());
    }


}
