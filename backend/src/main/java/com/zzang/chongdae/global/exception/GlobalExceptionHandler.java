package com.zzang.chongdae.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handle(MarketException e) {
        ErrorResponse errorResponse = e.getErrorResponse();
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse.getErrorMessage());
    }
}
