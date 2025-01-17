package com.zzang.chongdae.auth.config;

import com.zzang.chongdae.auth.service.AuthClient;
import com.zzang.chongdae.auth.service.StubAuthClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestAuthClientConfig {

    @Bean
    AuthClient testAuthConfig() {
        return new StubAuthClient();
    }
}
