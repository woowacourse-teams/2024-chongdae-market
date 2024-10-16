package com.zzang.chongdae.notification.config;

import com.zzang.chongdae.notification.service.FakeNotificationSender;
import com.zzang.chongdae.notification.service.NotificationSender;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestNotificationConfig {

    @Bean
    @Primary
    public NotificationSender notificationSender() {
        return new FakeNotificationSender();
    }
}
