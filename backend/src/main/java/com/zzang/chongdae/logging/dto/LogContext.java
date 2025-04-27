package com.zzang.chongdae.logging.dto;

import com.zzang.chongdae.logging.domain.MemberIdentifier;

public record LogContext(String identifier,
                         MemberIdentifier memberIdentifier,
                         String latency,
                         String httpMethod,
                         String uri,
                         String requestBody,
                         String statusCode,
                         String responseBody,
                         String stackTrace) {
}
