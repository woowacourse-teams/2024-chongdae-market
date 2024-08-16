package com.zzang.chongdae.offering.config;

import com.zzang.chongdae.offering.service.NaverApiProductImageExtractor;
import com.zzang.chongdae.offering.service.ProductImageExtractor;
import com.zzang.chongdae.offering.util.httpclient.naver.NaverScrapClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OfferingConfig {

    private static final int CRAWLER_TIMEOUT_MILLISECONDS = 10000;

    @Bean
    public NaverScrapClient naverScrapClient() {
        return new NaverScrapClient(CRAWLER_TIMEOUT_MILLISECONDS);
    }

    @Bean
    public ProductImageExtractor productImageExtractor() {
        return new NaverApiProductImageExtractor(naverScrapClient());
    }
}
