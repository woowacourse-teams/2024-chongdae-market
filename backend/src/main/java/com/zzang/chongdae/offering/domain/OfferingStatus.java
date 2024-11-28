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

    public OfferingCondition decideOfferingCondition() {
        return OfferingCondition.decideBy(this);
    }

    public boolean isCountFull() {
        return this.totalCount == this.currentCount;
    }

    public boolean isCountNotFull() {
        return !isCountFull();
    }

    public boolean isCountAlmostFull() {
        if (totalCount <= 3) {
            return (totalCount - currentCount) < 2;
        }
        return (totalCount - currentCount) < 3;
    }

    public boolean isDeadlineAlmostOver() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = deadline.minusHours(6);
        return threshold.isBefore(now) && now.isBefore(deadline); // deadline - 6 < now < deadline
    }

    public boolean isDeadlineOver() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(this.deadline) || now.isEqual(this.deadline);
    }

    public boolean isDeadlineNotOver() {
        return LocalDateTime.now().isBefore(this.deadline);
    }

    public boolean isAutoConfirmed() {
        return isCountFull() && isDeadlineOver();
    }

    public boolean isManualConfirmed() {
        return isManualConfirmed;
    }

    public boolean isOpen() {
        OfferingCondition offeringCondition = decideOfferingCondition();
        return offeringCondition.isOpen();
    }

    public boolean isClosed() {
        return !isOpen();
    }
}
