package com.zzang.chongdae.offering.service.dto;

import com.zzang.chongdae.offering.domain.OfferingFilter;
import com.zzang.chongdae.offering.domain.OfferingFilterType;

public record OfferingFilterResponseItem(OfferingFilter key, String name, OfferingFilterType type) {

    public OfferingFilterResponseItem(OfferingFilter filter) {
        this(filter, filter.getName(), filter.getType());
    }
}
