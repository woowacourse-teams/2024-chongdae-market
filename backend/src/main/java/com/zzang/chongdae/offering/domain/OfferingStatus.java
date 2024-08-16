package com.zzang.chongdae.offering.domain;

public enum OfferingStatus {

    FULL,
    IMMINENT,
    CONFIRMED,
    AVAILABLE;

    public static OfferingStatus decideBy(OfferingCondition offeringCondition) {
        if (offeringCondition.isManualConfirmed() || offeringCondition.isAutoConfirmed()
                || (offeringCondition.isMeetingDateOver() && offeringCondition.isCountNotFull())) {
            return CONFIRMED; // TODO: isManualConfirmed() || isMeetingDateOver()만 하면 됨
        }
        if (offeringCondition.isCountFull() && offeringCondition.isMeetingDateNotOver()) {
            return FULL; // TODO: isCountFull()만 하면 됨
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
