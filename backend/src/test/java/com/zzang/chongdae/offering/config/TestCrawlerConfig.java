package com.zzang.chongdae.offering.config;

import com.zzang.chongdae.offering.service.ProductImageExtractor;
import com.zzang.chongdae.offering.util.httpclient.FakeHtmlCrawler;
import com.zzang.chongdae.offering.util.httpclient.HtmlCrawler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestCrawlerConfig {

    @Bean
    @Primary
    public HtmlCrawler testHtmlCrawler() {
        return new FakeHtmlCrawler();
    }

    @Bean
    @Primary
    public ProductImageExtractor testProductImageExtractor() {
        return new ProductImageExtractor(testHtmlCrawler());
    }
}
