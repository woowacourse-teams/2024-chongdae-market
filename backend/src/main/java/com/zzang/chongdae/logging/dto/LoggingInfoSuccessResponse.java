package com.zzang.chongdae.logging.dto;

public record LoggingInfoSuccessResponse(String identifier,
                                         String memberIdentifier,
                                         String httpMethod,
                                         String uri,
                                         String requestBody,
                                         String statusCode,
                                         String latency) {
}
