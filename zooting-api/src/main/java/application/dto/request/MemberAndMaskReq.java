package application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "마스크 구매시 요청 DTO")
public record MemberAndMaskReq(
        @Schema(description = "마스크 아이디")
        @NotNull
        @Min(value=0, message = "마스크 아이디의 최소값은 0입니다.")
        Long maskId
) {
}
