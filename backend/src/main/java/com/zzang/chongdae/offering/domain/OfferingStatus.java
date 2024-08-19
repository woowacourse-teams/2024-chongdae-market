package com.zzang.chongdae.offering.domain;

public enum OfferingStatus {

    FULL,
    IMMINENT,
    CONFIRMED,
    AVAILABLE;

    public static OfferingStatus decideByJoinedCount(OfferingJoinedCount offeringJoinedCount) {
        if (offeringJoinedCount.isCountFull()) {
            return FULL;
        }
        if (offeringJoinedCount.isCountAlmostFull()) {
            return IMMINENT;
        }
        return AVAILABLE;
    }

    public boolean isOpen() {
        return this == AVAILABLE || this == IMMINENT;
    }

    public boolean isClosed() {
        return this == CONFIRMED || this == FULL;
    }
}
