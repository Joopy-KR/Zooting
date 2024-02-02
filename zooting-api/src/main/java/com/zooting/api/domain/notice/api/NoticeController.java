package com.zooting.api.domain.notice.api;

import com.zooting.api.domain.notice.application.NoticeService;
import com.zooting.api.domain.notice.dto.request.NoticeSaveReq;
import com.zooting.api.domain.notice.dto.request.NoticeUpdateReq;
import com.zooting.api.domain.notice.dto.response.NoticeRes;
import com.zooting.api.global.common.BaseResponse;
import com.zooting.api.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
@Tag(name = "공지사항", description = "Notice 관련 API")
public class NoticeController {

    private final NoticeService noticeService;
    @Operation(summary = "공지사항 저장")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BaseResponse<String>> saveNotice(
            @Valid @RequestBody NoticeSaveReq noticeReq) {
        noticeService.saveNotice(noticeReq);
        return BaseResponse.success(
                SuccessCode.INSERT_SUCCESS,
                "공지사항 저장 성공"
        );
    }
    @Operation(summary = "공지사항 수정")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping
    public ResponseEntity<BaseResponse<String>> updateNotice(
            @Valid @RequestBody NoticeUpdateReq noticeReq) {
        noticeService.updateNotice(noticeReq);
        return BaseResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                "공지사항 수정 성공"
        );
    }

    @Operation(summary = "공지사항 조회")
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<BaseResponse<List<NoticeRes>>> selectNotice(
            @PageableDefault(sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        var result = noticeService.findNotice(pageable);
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                result
        );
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping
    @Operation(summary = "공지사항 삭제")
    public ResponseEntity<BaseResponse<String>> deleteBlockMember(
            @Valid @RequestParam(name="noticeId") Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return BaseResponse.success(
                SuccessCode.DELETE_SUCCESS,
                "공지사항 삭제 완료"
        );
    }
}
