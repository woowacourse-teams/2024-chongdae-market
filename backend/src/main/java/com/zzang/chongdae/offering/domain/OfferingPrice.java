package com.zzang.chongdae.offering.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OfferingPrice {

    private final Integer totalCount;
    private final BigDecimal totalPrice;

    public BigDecimal calculateDividedPrice() {
        return totalPrice.divide(BigDecimal.valueOf(totalCount), RoundingMode.HALF_UP);
    }
}
