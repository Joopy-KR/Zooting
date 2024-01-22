package application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "차단 추가, 해제 시 요청 DTO")
public record MemberAndBlockReq(
        @Schema(description = " 차단 대상 닉네임")
                @NotNull
                @Size(min = 2, max= 16, message = "닉네임은 2~16자입니다.")
        String nickname
) {
}
