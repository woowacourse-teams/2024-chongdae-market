package com.zzang.chongdae.offering.domain;

public enum OfferingStatus {

    FULL,
    IMMINENT,
    CONFIRMED,
    AVAILABLE;

    public static OfferingStatus decideBy(OfferingCondition offeringCondition) {
        if (offeringCondition.isManualConfirmed() || offeringCondition.isAutoConfirmed()
                || (offeringCondition.isMeetingDateOver() && offeringCondition.isCountNotFull())) {
            return CONFIRMED;
        }
        if (offeringCondition.isCountFull() && offeringCondition.isMeetingDateNotOver()) {
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
}
