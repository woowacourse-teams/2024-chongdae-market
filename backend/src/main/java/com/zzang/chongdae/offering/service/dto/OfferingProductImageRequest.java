package com.zzang.chongdae.offering.service.dto;

import jakarta.validation.constraints.NotEmpty;

public record OfferingProductImageRequest(
        @NotEmpty
        String productUrl
) {
}
