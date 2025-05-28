package com.zzang.chongdae.analytic.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VariantTypeTest {

    @DisplayName("맴버 아이디가 홀수 이면 B 그룹을 반환한다.")
    @Test
    void should_returnBGroup_when_givenMemberIdOdd() {
        MemberEntity member = new MemberEntity(1L, null, null, null, null, null);
        VariantType expected = VariantType.B;

        VariantType actual = VariantType.calculateByMember(member);

        assertEquals(actual, expected);
    }

    @DisplayName("맴버 아이디가 짝수 이면 A 그룹을 반환한다.")
    @Test
    void should_returnAGroup_when_givenMemberIdEven() {
        MemberEntity member = new MemberEntity(2L, null, null, null, null, null);
        VariantType expected = VariantType.A;

        VariantType actual = VariantType.calculateByMember(member);

        assertEquals(actual, expected);
    }
}