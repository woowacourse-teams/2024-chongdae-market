package com.zzang.chongdae.notification.service;

import com.zzang.chongdae.notification.exception.NotificationRetryableException;
import java.time.LocalTime;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Retryable(
        retryFor = NotificationRetryableException.class,
        backoff = @Backoff(delay = 1000, multiplier = 2, random = true)
)
@Component
public class RetryableTester {

    private int count = 0;

    public void throwNotificationRetryableException() {
        System.out.printf("시도: %s, 시간: %s \n", ++count, LocalTime.now());
        throw new NotificationRetryableException(null);
    }
}
