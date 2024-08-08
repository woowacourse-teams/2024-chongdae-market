package com.zzang.chongdae.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handle(MethodArgumentNotValidException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity
                .badRequest()
                .body(errorMessage);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handle(MissingServletRequestParameterException e) {
        ErrorMessage errorMessage = new ErrorMessage(
                "해당 쿼리 파리미터 값이 존재하지 않습니다 :[%s]".formatted(e.getParameterName()));
        return ResponseEntity
                .badRequest()
                .body(errorMessage);
    }
}
