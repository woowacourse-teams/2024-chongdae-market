package com.zzang.chongdae.offering.domain;

public enum OfferingCondition {

    FULL,
    IMMINENT,
    CONFIRMED,
    AVAILABLE;

    public static OfferingCondition decideBy(OfferingStatus offeringStatus) {
        if (offeringStatus.isCountFull() && offeringStatus.isDeadlineNotOver()) {
            return FULL;
        }
        if (offeringStatus.isCountAlmostFull() || offeringStatus.isDeadlineAlmostOver()) {
            return IMMINENT;
        }
        if (offeringStatus.isManualConfirmed() || offeringStatus.isAutoConfirmed()
                || (offeringStatus.isDeadlineOver() && offeringStatus.isCountNotFull())) {
            return CONFIRMED;
        }
        return AVAILABLE;
    }

    public boolean isOpen() {
        return this == AVAILABLE || this == IMMINENT;
    }
}
