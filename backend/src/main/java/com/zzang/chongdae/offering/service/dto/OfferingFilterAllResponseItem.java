package com.zzang.chongdae.offering.service.dto;

import com.zzang.chongdae.offering.domain.OfferingFilter;
import com.zzang.chongdae.offering.domain.OfferingFilterType;

public record OfferingFilterAllResponseItem(OfferingFilter keyword, String name, OfferingFilterType type) {

    public OfferingFilterAllResponseItem(OfferingFilter filter) {
        this(filter, filter.getName(), filter.getType());
    }
}
