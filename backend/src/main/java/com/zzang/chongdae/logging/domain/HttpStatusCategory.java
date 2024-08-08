package com.zzang.chongdae.logging.domain;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.logging.exception.LoggingErrorCode;
import java.util.Arrays;

public enum HttpStatusCategory {
    INFO_SUCCESS(200, 299),
    INFO_FAIL(400, 499),
    WARN(500, 599);

    private final int min;
    private final int max;

    HttpStatusCategory(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public static HttpStatusCategory fromStatusCode(int statusCode) {
        return Arrays.stream(values())
                .filter(category -> statusCode >= category.min && statusCode <= category.max)
                .findFirst()
                .orElseThrow(() -> new MarketException(LoggingErrorCode.INVALID_STATUS));
    }
}
