package com.zzang.chongdae.offering.domain;

public enum OfferingStatus {

    FULL,
    IMMINENT,
    CONFIRMED,
    AVAILABLE;

    public static OfferingStatus decideBy(OfferingCondition offeringCondition) {
        if (offeringCondition.isManualConfirmed() || (offeringCondition.isMeetingDateOver())) {
            return CONFIRMED;
        }
        if (offeringCondition.isCountFull()) {
            return FULL;
        }
        if (offeringCondition.isCountAlmostFull() || offeringCondition.isMeetingDateAlmostOver()) {
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
