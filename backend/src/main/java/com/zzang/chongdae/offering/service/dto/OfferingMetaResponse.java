package com.zzang.chongdae.offering.service.dto;

import com.zzang.chongdae.offering.repository.entity.OfferingEntity;

public record OfferingMetaResponse(String title, String thumbnailUrl) {

    private static final String DEFAULT_RESOURCE_FILE_NAME = "no-image.png";

    public OfferingMetaResponse(OfferingEntity offering, String resourceHost) {
        this(offering.getTitle(), getOrDefault(offering.getThumbnailUrl(), resourceHost));
    }

    private static String getOrDefault(String thumbnailUrl, String resourceHost) {
        if (thumbnailUrl == null) {
            return "https://%s/common/%s".formatted(resourceHost, DEFAULT_RESOURCE_FILE_NAME);
        }
        return thumbnailUrl;
    }
}
