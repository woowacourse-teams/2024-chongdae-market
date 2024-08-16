package com.zzang.chongdae.offering.config;

import com.zzang.chongdae.offering.service.OgTagProductImageExtractor;
import com.zzang.chongdae.offering.util.httpclient.crawler.FakeHtmlCrawler;
import com.zzang.chongdae.offering.util.httpclient.crawler.HtmlCrawler;
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
    public OgTagProductImageExtractor testProductImageExtractor() {
        return new OgTagProductImageExtractor(testHtmlCrawler());
    }
}
