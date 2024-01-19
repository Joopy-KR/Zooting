package com.zooting.api.global.common;

import com.zooting.api.global.common.code.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Objects;

@Schema(description = "Error Response")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    @Schema(description = "HTTP 에러 상태")
    private int status;
    @Schema(description = "Project 에러 코드")
    private String code;
    @Schema(description = "Project 에러 메시지")
    private String message;
    @Schema(description = "에러 발생 원인")
    private String reason;
    @Schema(description = "에러 Field 리스트")
    private List<FieldError> errors;

    @Builder(builderMethodName = "of")
    protected ErrorResponse(final ErrorCode code, final List<FieldError> errors, final String message) {
        Objects.requireNonNull(code);
        this.status = code.getStatus();
        this.code = code.getDivisionCode();
        this.message = code.getMessage();
        this.errors = Objects.isNull(errors) ? List.of() : errors;
        this.reason = Objects.isNull(message) ? "" : message;
    }
}
