package com.zzang.chongdae.offering.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OfferingCondition {

    private final LocalDateTime meetingDate;
    private final int totalCount;
    private final boolean isManualConfirmed;
    private final int currentCount;

    public OfferingStatus decideOfferingStatus() {
        return OfferingStatus.decideBy(this);
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

    public boolean isMeetingDateAlmostOver() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = meetingDate.minusHours(6);
        return threshold.isBefore(now) && now.isBefore(meetingDate);
    }

    public boolean isMeetingDateOver() {
        return !isMeetingDateNotOver();
    }

    public boolean isMeetingDateNotOver() {
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(this.meetingDate);
    }

    public boolean isAutoConfirmed() {
        return isCountFull() && isMeetingDateOver();
    }

    public boolean isManualConfirmed() {
        return isManualConfirmed;
    }

    public boolean isOpen() {
        OfferingStatus offeringStatus = decideOfferingStatus();
        return offeringStatus.isOpen();
    }

    public boolean isClosed() {
        return !isOpen();
    }
}
