package com.zzang.chongdae.offering.config;

import com.zzang.chongdae.offering.service.ProductImageExtractor;
import com.zzang.chongdae.offering.util.httpclient.HtmlCrawler;
import com.zzang.chongdae.offering.util.httpclient.JsoupHtmlCrawler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OfferingConfig {

    private static final int CRAWLER_READ_TIMEOUT = 1000;

    @Bean
    public HtmlCrawler htmlCrawler() {
        return new JsoupHtmlCrawler(CRAWLER_READ_TIMEOUT);
    }

    @Bean
    public ProductImageExtractor productImageExtractor() {
        return new ProductImageExtractor(htmlCrawler());
    }
}
