package com.zzang.chongdae.offeringmember.service.dto;

import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.domain.OfferingCondition;

public record ParticipationResponse(OfferingStatus offeringStatus, Integer currentCount) {

    public ParticipationResponse(OfferingCondition offeringCondition) {
        this(offeringCondition.decideOfferingStatus(),
                offeringCondition.getCurrentCount()
        );
    }
}
