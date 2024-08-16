package com.zzang.chongdae.offering.service.dto;

import com.zzang.chongdae.offering.domain.OfferingPrice;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;

public record OfferingAllResponseItem(Long id,
                                      String title,
                                      String meetingAddressDong,
                                      Integer currentCount,
                                      Integer totalCount,
                                      String thumbnailUrl,
                                      Integer dividedPrice,
                                      Integer originPrice,
                                      Double discountRate,
                                      OfferingStatus status,
                                      Boolean isOpen) {

    public OfferingAllResponseItem(
            OfferingEntity offering, OfferingPrice offeringPrice) {
        this(offering.getId(),
                offering.getTitle(),
                offering.getMeetingAddressDong(),
                offering.getCurrentCount(),
                offering.getTotalCount(),
                offering.getThumbnailUrl(),
                offeringPrice.calculateDividedPrice(),
                offeringPrice.getOriginPrice(),
                offering.getDiscountRate(),
                offering.getOfferingStatus(),
                offering.getOfferingStatus().isOpen()
        );
    }
}
