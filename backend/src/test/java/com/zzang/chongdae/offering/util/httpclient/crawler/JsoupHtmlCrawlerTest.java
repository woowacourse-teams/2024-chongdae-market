package com.zzang.chongdae.offering.util.httpclient.crawler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsoupHtmlCrawlerTest {

    @DisplayName("url을 통해 데이터를 가져오는데 실패할 경우 빈 값이 반환된다.")
    @Test
    void should_return_emptyString_when_failScraping() {
        // given
        JsoupHtmlCrawler crawler = new JsoupHtmlCrawler(1);
        String expected = "";

        // when
        String actual = crawler.scrap("someUrl");

        // then
        assertEquals(actual, expected);
    }

    @DisplayName("html 에서 Open Graph 속성을 추출할 수 있다.")
    @Test
    void should_return_openGraphProperty_when_given_html() {
        // given
        JsoupHtmlCrawler crawler = new JsoupHtmlCrawler(1000);
        String html = """
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
        String expected = "https://img1.daumcdn.net/thumb/S1200x630/?fname=https://t1.daumcdn.net/news/202407/31/imbc/20240731193413945rfaw.jpg";

        // when
        String actual = crawler.parseOpenGraph(html, "og:image");

        // then
        assertEquals(actual, expected);
    }

    @DisplayName("html 에서 Open Graph 속성을 추출할 수 없는 경우 빈 값을 반환한다.")
    @Test
    void should_return_emptyString_when_cannot_extract_property() {
        // given
        JsoupHtmlCrawler crawler = new JsoupHtmlCrawler(1000);
        String html = """
                <html>
                <head>
                </head>
                </html>
                """;
        String expected = "";

        // when
        String actual = crawler.parseOpenGraph(html, "og:image");

        // then
        assertEquals(actual, expected);
    }
}
