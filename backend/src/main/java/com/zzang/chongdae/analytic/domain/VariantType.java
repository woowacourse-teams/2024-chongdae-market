package com.zzang.chongdae.analytic.domain;

public enum VariantType {
    A,
    B;

    public static VariantType calculateByMemberId(Long memberId) {
        if (memberId % 2 == 0) {
            return A;
        }
        return B;
    }
}
