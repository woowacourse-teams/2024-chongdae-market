package com.zzang.chongdae.analytic.domain;

import com.zzang.chongdae.member.repository.entity.MemberEntity;

public enum VariantType {
    A,
    B;

    public static VariantType calculateByMember(MemberEntity member) {
        Long memberId = member.getId();
        if (memberId % 2 == 0) {
            return A;
        }
        return B;
    }
}
