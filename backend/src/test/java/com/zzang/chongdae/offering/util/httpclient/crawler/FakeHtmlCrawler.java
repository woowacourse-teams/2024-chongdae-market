package com.zzang.chongdae.offering.util.httpclient.crawler;

public class FakeHtmlCrawler implements HtmlCrawler {

    @Override
    public String scrap(String productUrl) {
        if (productUrl.equals("http://fail-product-url.com")) {
            return "";
        }
        return """
                <html>
                <head>
                    <meta property="og:site_name" content="다음 - MBC">
                    <meta property="og:title" content="편의점서도 파리 현지서도‥&quot;'올림픽 특수' 노려라&quot;">
                    <meta property="og:regDate" content="20240731193413">
                    <meta property="og:type" content="article">
                    <meta property="og:article:author" content="MBC">
                    <meta property="og:url" content="https://v.daum.net/v/20240731193413692">
                    <meta property="og:image" content="https://img1.daumcdn.net/thumb/S1200x630/?fname=https://t1.daumcdn.net/news/202407/31/imbc/20240731193413945rfaw.jpg">
                </head>
                </html>
                """;
    }

    @Override
    public String parseOpenGraph(String html, String property) {
        if (html.isEmpty()) {
            return "";
        }
        return "https://img1.daumcdn.net/thumb/S1200x630/?fname=https://t1.daumcdn.net/news/202407/31/imbc/20240731193413945rfaw.jpg";
    }
}
