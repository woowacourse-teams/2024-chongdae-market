package com.zzang.chongdae.offering.config;

import com.zzang.chongdae.offering.util.FakeStorageService;
import com.zzang.chongdae.storage.service.StorageService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestStorageConfig {

    @Bean
    @Primary
    public StorageService testStorageService() {
        return new FakeStorageService();
    }
}
