package com.zzang.chongdae.offering.service;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CombinationProductImageExtractor implements ProductImageExtractor {

    private final List<ProductImageExtractor> extractors;

    @Override
    public String extract(String productUrl) {
        for (ProductImageExtractor extractor : extractors) {
            String imageUrl = extractor.extract(productUrl);
            if (isExtractSuccess(imageUrl)) {
                return imageUrl;
            }
        }
        return "";
    }

    private boolean isExtractSuccess(String imageUrl) {
        return !imageUrl.isEmpty();
    }
}
