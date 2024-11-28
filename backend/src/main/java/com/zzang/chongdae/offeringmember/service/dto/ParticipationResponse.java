package com.zzang.chongdae.offeringmember.service.dto;

import com.zzang.chongdae.offering.domain.OfferingCondition;
import com.zzang.chongdae.offering.domain.OfferingStatus;

public record ParticipationResponse(OfferingCondition offeringCondition, Integer currentCount) {

    public ParticipationResponse(OfferingStatus offeringStatus) {
        this(offeringStatus.decideOfferingCondition(),
                offeringStatus.getCurrentCount()
        );
    }
}
