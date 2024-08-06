package com.zzang.chongdae.member.config;

import com.zzang.chongdae.member.service.FixedNicknameWordPicker;
import com.zzang.chongdae.member.service.NicknameWordPicker;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestNicknameWordPickerConfig {

    @Bean
    @Primary
    NicknameWordPicker testNicknameWordPicker() {
        return new FixedNicknameWordPicker();
    }
}
