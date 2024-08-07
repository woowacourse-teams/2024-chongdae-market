package com.zzang.chongdae.offering.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OfferingPrice {

    private final int totalCount;
    private final int totalPrice;
    private final Integer eachPrice;

    public int calculateDividedPrice() {
        return BigDecimal.valueOf(totalPrice)
                .divide(BigDecimal.valueOf(totalCount), RoundingMode.HALF_UP)
                .intValue();
    }

    public double calculateDiscountRate() {
        return (double) (eachPrice - totalPrice / totalCount) / eachPrice;
    }
}
