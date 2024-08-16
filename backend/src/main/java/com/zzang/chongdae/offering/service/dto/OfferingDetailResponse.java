package com.zzang.chongdae.offering.service.dto;

import com.zzang.chongdae.offering.domain.OfferingCondition;
import com.zzang.chongdae.offering.domain.OfferingPrice;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.time.LocalDateTime;

public record OfferingDetailResponse(Long id,
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
                                     String nickname,
                                     Boolean isProposer,
                                     Boolean isParticipated) {

    public OfferingDetailResponse(OfferingEntity offering,
                                  OfferingPrice offeringPrice,
                                  OfferingCondition offeringCondition,
                                  Boolean isProposer,
                                  Boolean isParticipated) {
        this(offering.getId(),
                offering.getTitle(),
                offering.getProductUrl(),
                offering.getMeetingAddress(),
                offering.getMeetingAddressDetail(),
                offering.getDescription(),
                offering.getMeetingDate(),
                offeringCondition.getCurrentCount(),
                offering.getTotalCount(),
                offering.getThumbnailUrl(),
                offeringPrice.calculateDividedPrice(),
                offering.getTotalPrice(),
                offeringCondition.decideOfferingStatus(),
                offering.getMember().getId(),
                offering.getMember().getNickname(),
                isProposer,
                isParticipated
        );
    }
}
