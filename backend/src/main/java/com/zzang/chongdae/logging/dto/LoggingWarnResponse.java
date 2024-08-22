package com.zzang.chongdae.logging.dto;

public record LoggingWarnResponse(String identifier,
                                  String memberIdentifier,
                                  String httpMethod,
                                  String uri,
                                  String requestBody,
                                  String statusCode,
                                  String errorMessage,
                                  String latency) {
}
