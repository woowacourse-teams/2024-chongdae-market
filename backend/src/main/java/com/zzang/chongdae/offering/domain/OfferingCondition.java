package com.zzang.chongdae.offering.domain;

public enum OfferingCondition {

    FULL,
    TIME_OUT,
    CONFIRMED,
    AVAILABLE;

    public static OfferingCondition decideBy(OfferingStatus offeringStatus) {
        if (offeringStatus.isManualConfirmed() || offeringStatus.isAutoConfirmed()) {
            return CONFIRMED;
        }
        if (offeringStatus.isCountFull()) {
            return FULL;
        }
        if (offeringStatus.isDeadlineOver()) {
            return TIME_OUT;
        }
        return AVAILABLE;
    }

    public boolean isOpen() {
        return this == AVAILABLE;
    }
}
