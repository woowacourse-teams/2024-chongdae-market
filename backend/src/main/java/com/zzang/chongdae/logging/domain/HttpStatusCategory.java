package com.zzang.chongdae.logging.domain;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.logging.dto.LogContext;
import com.zzang.chongdae.logging.exception.LoggingErrorCode;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@RequiredArgsConstructor
public enum HttpStatusCategory {

    INFO_SUCCESS(200, 299) {
        @Override
        public void log(Logger logger, LogContext context) {
            logger.info(formatLog(context));
        }
    },
    INFO_FAIL(400, 499) {
        @Override
        public void log(Logger logger, LogContext context) {
            logger.warn(formatLog(context));
        }
    },
    WARN(500, 599) {
        @Override
        public void log(Logger logger, LogContext context) {
            logger.error(formatLog(context));
        }
    };

    private static final String LOG_FORMAT = "trackId=%s, memberId=%s, method=%s, uri=%s, requestBody=%s, statusCode=%s, latency=%s, responseBody=%s, stackTrace=%s";
    private final int min;
    private final int max;

    public abstract void log(Logger logger, LogContext context);


    public static HttpStatusCategory fromStatusCode(int statusCode) {
        return Arrays.stream(values())
                .filter(category -> statusCode >= category.min && statusCode <= category.max)
                .findFirst()
                .orElseThrow(() -> new MarketException(LoggingErrorCode.INVALID_STATUS));
    }


    protected String formatLog(LogContext context) {
        return LOG_FORMAT.formatted(context.identifier(),
                context.memberIdentifier().getIdInfo(),
                context.httpMethod(),
                context.uri(),
                context.requestBody(),
                context.statusCode(),
                context.latency(),
                context.responseBody(),
                context.stackTrace());
    }
}
