package com.zooting.api.domain.mask.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "마스크 조회 요청시 페이지 정보를 담은 응답 DTO")
public record MaskPageRes(
        @Schema(description = "마스크 리스트")
        List<MaskRes> maskResList,
        @Schema(description = "현재 페이지")
        int currentPage,
        @Schema(description = "전체 페이지")
        int totalPage
) {
}
