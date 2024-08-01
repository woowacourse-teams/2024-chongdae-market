package com.zzang.chongdae.offering.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record OfferingSaveRequest(@NotNull
                                  Long memberId,

                                  @NotBlank
                                  String title,

                                  String productUrl,

                                  String thumbnailUrl,

                                  @NotNull
                                  Integer totalCount,

                                  @NotNull
                                  Integer totalPrice,

                                  Integer eachPrice,

                                  @NotBlank
                                  String meetingAddress,

                                  String meetingAddressDetail,

                                  String meetingAddressDong,

                                  @NotNull
                                  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                                  LocalDateTime deadline,

                                  @NotNull
                                  String description) {

    public OfferingEntity toEntity(MemberEntity member) {
        return new OfferingEntity(member, title, description, thumbnailUrl, productUrl, deadline, meetingAddress,
                meetingAddressDetail, meetingAddressDong, totalCount, 1, false, totalPrice, eachPrice);
    }
}
