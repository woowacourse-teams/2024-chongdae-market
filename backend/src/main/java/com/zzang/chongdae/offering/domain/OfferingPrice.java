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

    private static final int ROUNDING_SCALE = 1;

    private final int totalCount;
    private final int totalPrice;
    private final Integer originPrice;

    public int calculateDividedPrice() {
        return BigDecimal.valueOf(totalPrice)
                .divide(BigDecimal.valueOf(totalCount), RoundingMode.HALF_UP)
                .intValue();
    }

    public Double calculateDiscountRate() {
        validateOriginPrice();
        if (originPrice == null) {
            return null;
        }
        int dividedPrice = totalPrice / totalCount;
        double discountRate = (double) (originPrice - dividedPrice) / originPrice * 100;
        return roundHalfUp(discountRate, ROUNDING_SCALE);
    }

    private double roundHalfUp(double number, int roundingScale) {
        BigDecimal bigDecimal = new BigDecimal(number);
        bigDecimal = bigDecimal.setScale(roundingScale, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    private void validateOriginPrice() {
        int dividedPrice = totalPrice / totalCount;
        if (originPrice != null && originPrice < dividedPrice) {
            throw new MarketException(OfferingErrorCode.CANNOT_ORIGIN_PRICE_LESS_THEN_DIVIDED_PRICE);
        }
    }
}
