package com.zzang.chongdae.notification.config;

import com.zzang.chongdae.notification.service.FakeNotificationSender;
import com.zzang.chongdae.notification.service.FakeNotificationSubscriber;
import com.zzang.chongdae.notification.service.NotificationSender;
import com.zzang.chongdae.notification.service.NotificationSubscriber;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestNotificationConfig {

    @Bean
    @Primary
    public NotificationSender testNotificationSender() {
        return new FakeNotificationSender();
        // return new FcmNotificationSender();
    }

    @Bean
    @Primary
    public NotificationSubscriber testNotificationSubscriber() {
        return new FakeNotificationSubscriber();
        // return new FcmNotificationSubscriber();
    }
}
