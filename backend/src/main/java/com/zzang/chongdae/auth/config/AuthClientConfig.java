package com.zzang.chongdae.auth.config;

import com.zzang.chongdae.auth.service.AuthClient;
import com.zzang.chongdae.auth.service.RealAuthClient;
import com.zzang.chongdae.auth.service.TestAuthClient;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@Slf4j
public class AuthClientConfig {

    @Value("${auth.connect-timeout-length}")
    private Duration connectTimeoutLength;

    @Value("${auth.read-timeout-length}")
    private Duration readTimeoutLength;

    @Bean
    @Profile("prod")
    public AuthClient realAuthClient() {
        return new RealAuthClient(createRestClient());
    }

    @Bean
    @Profile({"default", "dev"})
    public AuthClient testAuthClient() {
        log.warn("테스트 인증 환경이 설정되었습니다. 프로덕션 환경이라면, 서버 중지가 필요합니다.");
        return new TestAuthClient();
    }

    private RestClient createRestClient() {
        return RestClient.builder()
                .requestFactory(createRequestFactory())
                .requestInterceptor(new AuthClientTimeoutInterceptor())
                .build();
    }

    private ClientHttpRequestFactory createRequestFactory() {
        ClientHttpRequestFactorySettings requestFactorySettings = ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(connectTimeoutLength)
                .withReadTimeout(readTimeoutLength);
        return ClientHttpRequestFactories.get(requestFactorySettings);
    }
}
