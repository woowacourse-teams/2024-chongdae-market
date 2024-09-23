package com.zzang.chongdae.offering.service.dto;

import com.zzang.chongdae.offering.domain.OfferingJoinedCount;
import com.zzang.chongdae.offering.domain.OfferingPrice;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.time.LocalDateTime;

public record OfferingUpdateResponse(
        Long id,
        String title,
        String productUrl,
        String meetingAddress,
        String meetingAddressDetail,
        String description,
        LocalDateTime meetingDate,
        Integer currentCount,
        Integer totalCount,
        String thumbnailUrl,
        Integer dividedPrice,
        Integer totalPrice,
        OfferingStatus status,
        Long memberId,
        String nickname
) {
    public OfferingUpdateResponse(OfferingEntity offering,
                                  OfferingPrice offeringPrice,
                                  OfferingJoinedCount offeringJoinedCount) {
        this(offering.getId(),
                offering.getTitle(),
                offering.getProductUrl(),
                offering.getMeetingAddress(),
                offering.getMeetingAddressDetail(),
                offering.getDescription(),
                offering.getMeetingDate(),
                offeringJoinedCount.getCurrentCount(),
                offering.getTotalCount(),
                offering.getThumbnailUrl(),
                offeringPrice.calculateDividedPrice(),
                offering.getTotalPrice(),
                offering.getOfferingStatus(),
                offering.getMember().getId(),
                offering.getMember().getNickname()
        );
    }
}
