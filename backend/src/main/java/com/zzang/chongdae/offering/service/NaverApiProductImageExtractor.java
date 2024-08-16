package com.zzang.chongdae.offering.service;

import com.zzang.chongdae.offering.util.httpclient.naver.NaverScrapClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NaverApiProductImageExtractor implements ProductImageExtractor {

    private final NaverScrapClient naverScrapClient;

    @Override
    public String extract(String productUrl) {
        return naverScrapClient.scrap(productUrl);
    }
}
