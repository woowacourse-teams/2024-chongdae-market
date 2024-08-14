package com.zzang.chongdae.offering.domain;

public enum OfferingStatus {

    FULL,
    IMMINENT,
    CONFIRMED,
    AVAILABLE;

    public static OfferingStatus decideBy(OfferingCondition offeringCondition) {
        if (offeringCondition.isManualConfirmed() || offeringCondition.isAutoConfirmed()
                || (offeringCondition.isDeadlineOver() && offeringCondition.isCountNotFull())) {
            return CONFIRMED;
        }
        if (offeringCondition.isCountFull() && offeringCondition.isDeadlineNotOver()) {
            return FULL;
        }
        if (offeringCondition.isCountAlmostFull() || offeringCondition.isDeadlineAlmostOver()) {
            return IMMINENT;
        }
        return AVAILABLE;
    }

    public boolean isOpen() {
        return this == AVAILABLE || this == IMMINENT;
    }
}
