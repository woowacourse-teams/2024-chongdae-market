package com.zzang.chongdae.offering.util.httpclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
                    .header("Host", parseHostFromProductUrl(productUrl))
                    .header("User-Agent",
                            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:128.0) Gecko/20100101 Firefox/128.0")
                    .header("Accept-Language", "en-US,en;q=0.9,ko;q=0.8")
                    .get()
                    .html();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String parseHostFromProductUrl(String productUrl) throws MalformedURLException {
        URL parsedUrl = new URL(productUrl);
        return parsedUrl.getHost();
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
