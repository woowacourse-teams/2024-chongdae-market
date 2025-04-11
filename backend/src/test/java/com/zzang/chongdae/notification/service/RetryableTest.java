package com.zzang.chongdae.notification.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.zzang.chongdae.global.service.ServiceTest;
import com.zzang.chongdae.notification.exception.NotificationRetryableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RetryableTest extends ServiceTest {

    @Autowired
    RetryableTester sender;

    @DisplayName("FCM에서 5xx 에러를 반환할 경우 3번 재시도한다.")
    @Test
    void name() {
        assertThatThrownBy(() -> sender.throwNotificationRetryableException())
                .isInstanceOf(NotificationRetryableException.class);
    }
}
