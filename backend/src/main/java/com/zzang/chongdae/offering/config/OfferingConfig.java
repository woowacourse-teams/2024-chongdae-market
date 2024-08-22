package com.zzang.chongdae.offering.config;

import com.zzang.chongdae.offering.service.CombinationProductImageExtractor;
import com.zzang.chongdae.offering.service.NaverApiProductImageExtractor;
import com.zzang.chongdae.offering.service.OgTagProductImageExtractor;
import com.zzang.chongdae.offering.service.ProductImageExtractor;
import com.zzang.chongdae.offering.util.httpclient.crawler.HtmlCrawler;
import com.zzang.chongdae.offering.util.httpclient.crawler.JsoupHtmlCrawler;
import com.zzang.chongdae.offering.util.httpclient.naver.NaverScrapClient;
import java.util.List;
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
    public HtmlCrawler htmlCrawler() {
        return new JsoupHtmlCrawler(CRAWLER_TIMEOUT_MILLISECONDS);
    }

    @Bean
    public ProductImageExtractor productImageExtractor() {
        List<ProductImageExtractor> extractors = List.of(
                new OgTagProductImageExtractor(htmlCrawler()),
                new NaverApiProductImageExtractor(naverScrapClient())
        );
        return new CombinationProductImageExtractor(extractors);
    }
}
