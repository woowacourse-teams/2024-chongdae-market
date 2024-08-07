package com.zzang.chongdae.offering.domain;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OfferingPrice {

    private final int totalCount;
    private final int totalPrice;
    private final Integer originPrice;

    public int calculateDividedPrice() {
        return BigDecimal.valueOf(totalPrice)
                .divide(BigDecimal.valueOf(totalCount), RoundingMode.HALF_UP)
                .intValue();
    }

    public double calculateDiscountRate() {
        return (double) (originPrice - totalPrice / totalCount) / originPrice;
    }

    public void validateOriginPrice() {
        int dividedPrice = totalPrice / totalCount;
        if (originPrice < dividedPrice) {
            throw new MarketException(OfferingErrorCode.CANNOT_ORIGIN_PRICE_LESS_THEN_DIVIDED_PRICE);
        }
    }
}
