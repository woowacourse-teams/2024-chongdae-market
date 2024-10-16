package com.zzang.chongdae.global.config;

import com.zzang.chongdae.member.config.TestNicknameWordPickerConfig;
import com.zzang.chongdae.notification.config.TestNotificationConfig;
import com.zzang.chongdae.offering.config.TestCrawlerConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@Import({TestCrawlerConfig.class,
        TestNicknameWordPickerConfig.class,
        TestClockConfig.class,
        TestNotificationConfig.class})
@TestConfiguration
public class TestConfig {
}
