package com.zzang.chongdae.offering.service;

import com.zzang.chongdae.offering.util.httpclient.crawler.HtmlCrawler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductImageExtractor {

    public static final String OG_IMAGE_PROPERTY = "og:image";

    private final HtmlCrawler htmlCrawler;

    public String extract(String productUrl) {
        String html = htmlCrawler.scrap(productUrl);
        return htmlCrawler.parseOpenGraph(html, OG_IMAGE_PROPERTY);
    }
}
