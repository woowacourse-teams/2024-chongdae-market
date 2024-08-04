package com.zzang.chongdae.offering.domain;

import static com.zzang.chongdae.offering.domain.OfferingFilterType.MULTIPLE;
import static com.zzang.chongdae.offering.domain.OfferingFilterType.SINGLE;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OfferingFilter {
    JOINABLE("참여가능만", MULTIPLE),
    IMMINENT("마감임박순", SINGLE),
    HIGH_DISCOUNT("높은할인률순", SINGLE);

    private final String name;
    private final OfferingFilterType type;
}
