package com.zooting.api.domain.background.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "전체 배경 이미지 조회 요청시 응답 DTO")
public record BackgroundPageRes(
        @Schema(description = "배경이미지 리스트")
        List<BackgroundRes> backgroundRes,
        @Schema(description = "현재 페이지")
        int currentPage,
        @Schema(description = "전체 페이지")
        int totalPage
) {
}
