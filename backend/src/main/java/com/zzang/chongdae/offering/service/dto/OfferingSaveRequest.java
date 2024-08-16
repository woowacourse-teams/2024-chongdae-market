package com.zzang.chongdae.offering.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.CommentRoomStatus;
import com.zzang.chongdae.offering.domain.OfferingPrice;
import com.zzang.chongdae.offering.domain.OfferingStatus;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record OfferingSaveRequest(@NotBlank
                                  String title,

                                  String productUrl,

                                  String thumbnailUrl,

                                  @NotNull
                                  Integer totalCount,

                                  @NotNull
                                  Integer totalPrice,

                                  Integer originPrice,

                                  @NotBlank
                                  String meetingAddress,

                                  String meetingAddressDetail,

                                  String meetingAddressDong,

                                  @NotNull
                                  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                                  LocalDateTime meetingDate,

                                  @NotNull
                                  String description) {

    public OfferingEntity toEntity(MemberEntity member) {
        OfferingPrice offeringPrice = new OfferingPrice(totalCount, totalPrice, originPrice);
        Double discountRate = offeringPrice.calculateDiscountRate();

        return new OfferingEntity(member, title, description, thumbnailUrl, productUrl, meetingDate, meetingAddress,
                meetingAddressDetail, meetingAddressDong, totalCount, 1, false,
                totalPrice, originPrice, discountRate, OfferingStatus.AVAILABLE, CommentRoomStatus.GROUPING);
        // TODO : 각자 맡은 팀에서 계산하는 로직 생각하기, 객체로 빼서 만드셈~
    }
}
