package com.zzang.chongdae.offering.service;

import com.zzang.chongdae.offering.util.httpclient.crawler.HtmlCrawler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OgTagProductImageExtractor implements ProductImageExtractor {

    public static final String OG_IMAGE_PROPERTY = "og:image";

    private final HtmlCrawler htmlCrawler;

    @Override
    public String extract(String productUrl) {
        String html = htmlCrawler.scrap(productUrl);
        return htmlCrawler.parseOpenGraph(html, OG_IMAGE_PROPERTY);
    }
}
