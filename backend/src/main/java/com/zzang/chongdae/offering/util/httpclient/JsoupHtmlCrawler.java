package com.zzang.chongdae.offering.util.httpclient;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@RequiredArgsConstructor
public class JsoupHtmlCrawler implements HtmlCrawler {

    private final int timeoutMilliseconds;

    @Override
    public String scrap(String productUrl) {
        try {
            return Jsoup.connect(productUrl)
                    .timeout(timeoutMilliseconds)
                    .header("Accept-Language", "en-US,en;q=0.9,ko;q=0.8")
                    .get()
                    .html();
        } catch (IOException | RuntimeException e) {
            return "";
        }
    }

    @Override
    public String parseOpenGraph(String html, String property) {
        Document document = Jsoup.parse(html);
        Element head = document.head();
        Elements meta = head.getElementsByTag("meta");
        return meta.stream()
                .filter(element -> element.hasAttr("property"))
                .filter(element -> element.attribute("property").getValue().equals(property))
                .map(element -> element.attribute("content").getValue())
                .findFirst()
                .orElse("");
    }
}
