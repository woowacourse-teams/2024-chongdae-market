package com.zzang.chongdae.offering.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OfferingStatus {

    private final LocalDateTime deadline;
    private final int totalCount;
    private final boolean isManualConfirmed;
    private final int currentCount;

    public OfferingCondition decideOfferingStatus() {
        if (isCountFull()) {
            return OfferingCondition.FULL;
        }
        if (isDeadlineOver()) {
            return OfferingCondition.TIME_OUT;
        }
        if (isManualConfirmed() || isAutoConfirmed()) {
            return OfferingCondition.CONFIRMED;
        }
        return OfferingCondition.AVAILABLE;
    }

    private boolean isCountFull() {
        return this.totalCount == this.currentCount;
    }

    private boolean isDeadlineOver() {
        return LocalDateTime.now().isAfter(this.deadline);
    }

    private boolean isAutoConfirmed() {
        return isCountFull() && isDeadlineOver();
    }

    private boolean isManualConfirmed() {
        return isManualConfirmed;
    }
}
