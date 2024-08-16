package com.zzang.chongdae.offering.util.httpclient.naver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

@RequiredArgsConstructor
public class NaverScrapClient {

    private static String API_END_POINT = "https://m.blog.naver.com/api/scraper?url=%s";

    private final int timeoutMilliseconds;

    public String scrap(String productUrl) {
        try {
            String response = connect(productUrl);
            return extractJson(response);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String connect(String productUrl) throws IOException {
        return Jsoup.connect(String.format(API_END_POINT, productUrl))
                .timeout(timeoutMilliseconds)
                .header("Host", "m.blog.naver.com")
                .header("Sec-Fetch-Mode", "cors")
                .header("Accept", "application/json, text/plain, */*")
                .userAgent(
                        "Mozilla/5.0 (iPhone; CPU iPhone OS 17_5_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.5 Mobile/15E148 Safari/605.1 NAVER(inapp; search; 2000; 12.7.2; SE2)")
                .header("Referer", "https://m.blog.naver.com/ScrapForm.naver?p__g=i__n")
                .header("Sec-Fetch-Dest", "empty")
                .header("Accept-Language", "ko-KR,ko;q=0.9")
                .method(Method.GET)
                .ignoreContentType(true)
                .get().body().html();
    }

    private String extractJson(String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);
        String imageUrl = rootNode
                .path("summary")
                .path("image")
                .path("url").asText();
        return removeHttpHttps(imageUrl);
    }

    public String removeHttpHttps(String url) {
        if (url.startsWith("http:")) {
            return url.substring("http:".length());
        }
        if (url.startsWith("https:")) {
            return url.substring("https:".length());
        }
        return url;
    }
}
