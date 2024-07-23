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

    public boolean isDeadlineOver() {
        return LocalDateTime.now().isAfter(this.deadline);
    }

    public boolean isAutoConfirmed() {
        return isCountFull() && isDeadlineOver();
    }

    public boolean isManualConfirmed() {
        return isManualConfirmed;
    }

    public OfferingStatus addParticipantCount() {
        return new OfferingStatus(deadline, totalCount, isManualConfirmed, currentCount + 1);
    }

    public boolean isOpen() {
        OfferingCondition offeringCondition = decideOfferingCondition();
        return offeringCondition.isOpen();
    }

    public boolean isClosed() {
        return !isOpen();
    }
}
