package com.zooting.api.global.exception;

import com.zooting.api.global.common.ErrorResponse;
import com.zooting.api.global.common.code.ErrorCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@Log4j2
@RestControllerAdvice
public class GlobalControllerAdvice {
    /**
     * 예외 처리 되지 않은 모든 에러 처리
     *
     * @param e Exception
     * @return ResponseEntity
     */
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleAllExceptions(RuntimeException e) {
        log.error(e.getMessage());
        ErrorResponse response = ErrorResponse.of()
                .code(ErrorCode.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodValidation(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        // Validation 에러 정보를 추출하거나 처리할 로직을 작성
        String errorMessage = "Validation error: " + bindingResult.getAllErrors().get(0).getDefaultMessage();

        ErrorResponse response = ErrorResponse.of()
                .code(ErrorCode.NOT_VALID_ERROR)
                .message(errorMessage)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidation(HandlerMethodValidationException ex) {
        ErrorResponse response = ErrorResponse.of()
                .code(ErrorCode.NOT_VALID_ERROR)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        // 원하는 응답 형식으로 ResponseEntity를 생성하여 반환
        ErrorResponse response = ErrorResponse.of()
                .code(ErrorCode.INTERNAL_SERVER_ERROR)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
