package com.zooting.api.global.exception;

import com.zooting.api.global.common.code.ErrorCode;

public class BaseExceptionHandler extends RuntimeException{
    private final ErrorCode errorCode;

    public BaseExceptionHandler(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public BaseExceptionHandler(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
