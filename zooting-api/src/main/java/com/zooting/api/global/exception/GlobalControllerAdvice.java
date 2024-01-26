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
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
        e.printStackTrace(); // 처리되지 않은 에러에 대해서는 서버 로그 출력
        ErrorResponse response = ErrorResponse.of()
                .code(ErrorCode.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleRuntimeExceptions(RuntimeException e) {
        e.printStackTrace(); // 처리되지 않은 에러에 대해서는 서버 로그 출력
        ErrorResponse response = ErrorResponse.of()
                .code(ErrorCode.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodValidation(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        ErrorResponse response = ErrorResponse.of()
                .code(ErrorCode.NOT_VALID_ERROR)
                .message(ex.getMessage())
                .errors(bindingResult.getFieldErrors())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidation(HandlerMethodValidationException ex) {
        ErrorResponse response = ErrorResponse.of()
                .code(ErrorCode.NOT_VALID_ERROR)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
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
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointException(NullPointerException ex) {
        // 원하는 응답 형식으로 ResponseEntity를 생성하여 반환
        ErrorResponse response = ErrorResponse.of()
                .code(ErrorCode.INTERNAL_SERVER_ERROR)
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
