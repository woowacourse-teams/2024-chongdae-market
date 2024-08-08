package com.zzang.chongdae.logging.dto;

public record LoggingErrorResponse(String identifier,
                                   String httpMethod,
                                   String uri,
                                   String requestBody,
                                   String statusCode,
                                   String errorMessage,
                                   String latency,
                                   String stacktrace) {
}
