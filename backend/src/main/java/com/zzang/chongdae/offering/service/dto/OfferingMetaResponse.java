package com.zzang.chongdae.offering.service.dto;

import com.zzang.chongdae.offering.repository.entity.OfferingEntity;

public record OfferingMetaResponse(String title, String thumbnailUrl) {

    private static final String DEFAULT_THUMBNAIL_URL = "https://image.chongdae.site/common/no-image.png";

    public OfferingMetaResponse(OfferingEntity offering) {
        this(offering.getTitle(), getOrDefault(offering.getThumbnailUrl()));
    }

    private static String getOrDefault(String thumbnailUrl) {
        if (thumbnailUrl == null) {
            return DEFAULT_THUMBNAIL_URL;
        }
        return thumbnailUrl;
    }
}
