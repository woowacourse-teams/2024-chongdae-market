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
    private final Integer eachPrice;

    public int calculateDividedPrice() {
        return BigDecimal.valueOf(totalPrice)
                .divide(BigDecimal.valueOf(totalCount), RoundingMode.HALF_UP)
                .intValue();
    }

    public double calculateDiscountRate() {
        return (double) (eachPrice - totalPrice / totalCount) / eachPrice;
    }

    public void validateEachPrice() {
        int dividedPrice = totalPrice / totalCount;
        if (dividedPrice > eachPrice) {
            throw new MarketException(OfferingErrorCode.CANNOT_DIVIDED_PRICE_GREATER_THEN_EACH_PRICE);
        }
    }
}
