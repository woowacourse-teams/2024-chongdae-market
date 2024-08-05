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

    JOINABLE("참여가능만", VISIBLE), // 처음: 참여 가능한 것 중 id가 가장 높은 값 10개 / 이후: 참여 가능한 것 중 마지막 id보다 낮은 것 x개 찾기
    IMMINENT("마감임박순", VISIBLE), // 처음: 현재 시간보다 큰 것 중 가장 작은 값 10개 / 이후: lastDeadline 보다 큰 것 x개 찾기
    HIGH_DISCOUNT("높은할인률순", VISIBLE), // 처음: 할인률(n빵가격/낱개가격) 가장 높은 값 10개 / 이후: 마지막 할인률(n빵가격/낱개가격)보다 낮은 것 x개 찾기
    RECENT("최신순", INVISIBLE); // 처음: id가 가장 높은 값 10개 / 이후: 마지막 id보다 낮은 것 x개 찾기

    private final String value;
    private final OfferingFilterType type;

    public static OfferingFilter findByName(String filter) {
        return Arrays.stream(values())
                .filter(f -> f.name().equals(filter))
                .findFirst()
                .orElseThrow(() -> new MarketException(OfferingErrorCode.NOT_FOUND_FILTER));
    }
}
