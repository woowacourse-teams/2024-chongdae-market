package com.zzang.chongdae.analytic.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VariantTypeTest {

    @DisplayName("멤버 아이디가 홀수 이면 B 그룹을 반환한다.")
    @Test
    void should_returnBGroup_when_givenMemberIdOdd() {
        Long memberId = 1L;
        VariantType expected = VariantType.B;

        VariantType actual = VariantType.calculateByMemberId(memberId);

        assertEquals(actual, expected);
    }

    @DisplayName("멤버 아이디가 짝수 이면 A 그룹을 반환한다.")
    @Test
    void should_returnAGroup_when_givenMemberIdEven() {
        Long memberId = 2L;
        VariantType expected = VariantType.A;

        VariantType actual = VariantType.calculateByMemberId(memberId);

        assertEquals(actual, expected);
    }
}
