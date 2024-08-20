package com.zzang.chongdae.offeringmember.service.dto;

import com.zzang.chongdae.offering.repository.entity.OfferingEntity;

public record ParticipantCountResponseItem(int currentCount, int totalCount) {

    public ParticipantCountResponseItem(OfferingEntity offering) {
        this(offering.getCurrentCount(), offering.getTotalCount());
    }
}
