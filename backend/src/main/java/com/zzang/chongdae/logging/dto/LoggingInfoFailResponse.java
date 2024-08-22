package com.zzang.chongdae.logging.dto;

public record LoggingInfoFailResponse(String identifier,
                                      String memberIdentifier,
                                      String httpMethod,
                                      String uri,
                                      String requestBody,
                                      String statusCode,
                                      String errorMessage,
                                      String latency) {
}
