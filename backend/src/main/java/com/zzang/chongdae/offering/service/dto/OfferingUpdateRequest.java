package com.zzang.chongdae.offering.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzang.chongdae.offering.domain.UpdatedOffering;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record OfferingUpdateRequest(
        @NotBlank
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
        String description
) {
    public UpdatedOffering toUpdatedOffering() {
        return new UpdatedOffering(title, productUrl, thumbnailUrl, totalCount, totalPrice, originPrice, meetingAddress,
                meetingAddressDetail, meetingAddressDong, meetingDate, description);
    }
}
