package com.zzang.chongdae.offering.domain;

public enum OfferingCondition {

    FULL,
    TIME_OUT,
    CONFIRMED,
    AVAILABLE;

    public static OfferingCondition decideBy(OfferingStatus offeringStatus) {
        if (offeringStatus.isCountFull()) {
            return OfferingCondition.FULL;
        }
        if (offeringStatus.isDeadlineOver()) {
            return OfferingCondition.TIME_OUT;
        }
        if (offeringStatus.isManualConfirmed() || offeringStatus.isAutoConfirmed()) {
            return OfferingCondition.CONFIRMED;
        }
        return OfferingCondition.AVAILABLE;
    }
}
