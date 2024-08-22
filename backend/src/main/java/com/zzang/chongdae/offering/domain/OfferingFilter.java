package com.zzang.chongdae.offering.domain;

import static com.zzang.chongdae.offering.domain.OfferingFilterType.INVISIBLE;
import static com.zzang.chongdae.offering.domain.OfferingFilterType.VISIBLE;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OfferingFilter {

    JOINABLE("참여가능만", VISIBLE),
    IMMINENT("마감임박만", VISIBLE),
    HIGH_DISCOUNT("높은할인율순", VISIBLE),
    RECENT("최신순", INVISIBLE);

    private final String value;
    private final OfferingFilterType type;

    public static OfferingFilter findByName(String filter) {
        return Arrays.stream(values())
                .filter(f -> f.name().equals(filter))
                .findFirst()
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND_FILTER));
    }
}
