package com.zzang.chongdae.global.exception;

import com.zzang.chongdae.logging.config.CachedHttpServletResponseWrapper;
import com.zzang.chongdae.logging.dto.LoggingErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
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
        ErrorMessage errorMessage = new ErrorMessage(
                "요청값이 유효하지 않습니다: [%s]".formatted(e.getAllErrors().get(0).getDefaultMessage()));
        return ResponseEntity
                .badRequest()
                .body(errorMessage);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handle(ConstraintViolationException e) {
        List<String> messages = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        ErrorMessage errorMessage = new ErrorMessage(
                "요청값이 유효하지 않습니다: [%s]".formatted(messages.get(0)));
        return ResponseEntity
                .badRequest()
                .body(errorMessage);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handle(HandlerMethodValidationException e) {
        ErrorMessage errorMessage = new ErrorMessage(
                "요청값이 유효하지 않습니다: [%s]".formatted(e.getAllErrors().get(0).getDefaultMessage()));
        return ResponseEntity
                .badRequest()
                .body(errorMessage);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handle(MissingServletRequestParameterException e) {
        ErrorMessage errorMessage = new ErrorMessage(
                "해당 쿼리 파리미터 값이 존재하지 않습니다: [%s]".formatted(e.getParameterName()));
        return ResponseEntity
                .badRequest()
                .body(errorMessage);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handle(HttpMessageNotReadableException e) {
        ErrorMessage errorMessage = new ErrorMessage(
                "유효하지 않은 요청 형태입니다: [%s]".formatted(e.getMessage().split(":")[0]));
        return ResponseEntity
                .badRequest()
                .body(errorMessage);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handle(NoResourceFoundException e) {
        ErrorMessage errorMessage = new ErrorMessage(
                "유효하지 않은 요청 형태입니다: [%s]".formatted(e.getMessage().split(":")[0]));
        return ResponseEntity
                .badRequest()
                .body(errorMessage);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handle(Exception e, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        ErrorMessage errorMessage = new ErrorMessage("서버 관리자에게 문의하세요");

        String identifier = UUID.randomUUID().toString();
        String httpMethod = request.getMethod();
        String uri = request.getRequestURI();
        String requestBody = new String(request.getInputStream().readAllBytes());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();

        CachedHttpServletResponseWrapper cachedResponse = (CachedHttpServletResponseWrapper) response;
        String responseBody = new String(cachedResponse.getCachedBody());

        long startTime = Long.parseLong(request.getAttribute("startTime").toString());
        long endTime = System.currentTimeMillis();
        String latency = endTime - startTime + "ms";

        LoggingErrorResponse logResponse = new LoggingErrorResponse(identifier, httpMethod, uri, requestBody,
                "500", responseBody, latency, stackTrace);
        log.error(logResponse.toString());

        return ResponseEntity
                .internalServerError()
                .body(errorMessage);
    }
}
