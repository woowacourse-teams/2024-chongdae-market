package com.zzang.chongdae.auth.config;

import com.zzang.chongdae.auth.service.AuthClient;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class AuthClientConfig {

    @Value("${auth.connect-timeout-length}")
    private Duration connectTimeoutLength;

    @Value("${auth.read-timeout-length}")
    private Duration readTimeoutLength;

    @Bean
    public AuthClient authClient() {
        return new AuthClient(createRestClient());
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
