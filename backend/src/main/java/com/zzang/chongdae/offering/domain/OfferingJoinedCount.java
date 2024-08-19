package com.zzang.chongdae.offering.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OfferingJoinedCount {

    private final int totalCount;
    private final int currentCount;

    public OfferingStatus decideOfferingStatus() {
        return OfferingStatus.decideByJoinedCount(this);
    }

    public boolean isCountFull() {
        return this.totalCount == this.currentCount;
    }

    public boolean isCountAlmostFull() {
        if (totalCount <= 3) {
            return (totalCount - currentCount) < 2;
        }
        return (totalCount - currentCount) < 3;
    }
}
