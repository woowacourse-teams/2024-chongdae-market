package com.zzang.chongdae.offering.util.httpclient.crawler;

public interface HtmlCrawler {

    String scrap(String productUrl);

    String parseOpenGraph(String html, String property);
}
