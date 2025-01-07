package com.zzang.chongdae.notification.config;

import com.zzang.chongdae.notification.service.NotificationSender;
import com.zzang.chongdae.notification.service.NotificationSubscriber;
import com.zzang.chongdae.notification.service.StubNotificationSender;
import com.zzang.chongdae.notification.service.StubNotificationSubscriber;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestNotificationConfig {

    @Bean
    @Primary
    public NotificationSender testNotificationSender() {
        return new StubNotificationSender();
        // return new FcmNotificationSender();
    }

    @Bean
    @Primary
    public NotificationSubscriber testNotificationSubscriber() {
        return new StubNotificationSubscriber();
        // return new FcmNotificationSubscriber();
    }
}
