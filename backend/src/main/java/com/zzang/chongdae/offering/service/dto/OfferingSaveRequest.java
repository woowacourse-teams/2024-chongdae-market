package com.zzang.chongdae.offering.service.dto;

import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.time.LocalDateTime;

public record OfferingSaveRequest(Long memberId,
                                  String title,
                                  String productUrl,
                                  String thumbnailUrl,
                                  Integer totalCount,
                                  Integer totalPrice,
                                  Integer eachPrice,
                                  String meetingAddress,
                                  String meetingAddressDetail,
                                  String meetingAddressDong,
                                  LocalDateTime deadline,
                                  String description) {

    public OfferingEntity toEntity(MemberEntity member) {
        return new OfferingEntity(member, title, description, thumbnailUrl, productUrl, deadline, meetingAddress,
                meetingAddressDetail, meetingAddressDong, totalCount, 1, false, totalPrice, eachPrice);
    }
}
