package application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "해금배경이미지 조회 응답시 DTO")
public record MemberAndBackgroundRes(
        @Schema(description = "파일이름")
        String fileName,
        @Schema(description = "이미지 url")
        String imgUrl,
        @Schema(description = "가격")
        Long price
) {
}
