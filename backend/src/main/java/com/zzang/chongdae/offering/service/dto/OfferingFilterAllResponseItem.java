package com.zzang.chongdae.offering.service.dto;

import com.zzang.chongdae.offering.domain.OfferingFilter;
import com.zzang.chongdae.offering.domain.OfferingFilterType;

public record OfferingFilterAllResponseItem(OfferingFilter name, String value, OfferingFilterType type) {

    public OfferingFilterAllResponseItem(OfferingFilter filter) {
        this(filter, filter.getValue(), filter.getType());
    }
}
