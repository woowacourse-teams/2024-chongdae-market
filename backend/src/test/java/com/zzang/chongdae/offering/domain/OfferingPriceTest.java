package com.zzang.chongdae.offering.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OfferingPriceTest {

    @DisplayName("할인율을 계산할 때 소수점 둘째 자리에서 반올림한다.")
    @Test
    void should_be_roundTwoDecimalPlaces_when_calculateDiscountRate() {
        // given
        OfferingPrice offeringPrice = new OfferingPrice(3, 10000, 5000);

        // when
        double actual = offeringPrice.calculateDiscountRate();

        // then
        double expected = 0.3;
        assertEquals(actual, expected);
    }
}
