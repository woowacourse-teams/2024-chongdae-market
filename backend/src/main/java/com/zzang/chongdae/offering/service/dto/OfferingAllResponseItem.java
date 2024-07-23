package com.zzang.chongdae.offering.service.dto;

import com.zzang.chongdae.offering.domain.OfferingCondition;
import com.zzang.chongdae.offering.domain.OfferingPrice;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;

public record OfferingAllResponseItem(Long id,
                                      String title,
                                      String meetingAddress,
                                      Integer currentCount,
                                      Integer totalCount,
                                      String thumbnailUrl,
                                      Integer dividedPrice,
                                      OfferingCondition condition,
                                      Boolean isClosed) {

    public OfferingAllResponseItem(
            OfferingEntity offering, OfferingPrice offeringPrice, OfferingStatus offeringStatus) {
        this(offering.getId(),
                offering.getTitle(),
                offering.getMeetingAddress(),
                offeringStatus.getCurrentCount(),
                offering.getTotalCount(),
                offering.getThumbnailUrl(),
                offeringPrice.calculateDividedPrice().intValue(),
                offeringStatus.decideOfferingCondition(),
                offeringStatus.decideOfferingCondition().isClosed()
        );
    }
}
