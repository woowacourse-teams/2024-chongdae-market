package com.zzang.chongdae.offering.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzang.chongdae.offering.domain.OfferingMeeting;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record OfferingMeetingUpdateRequest(@NotNull
                                           @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                                           LocalDateTime meetingDate,

                                           @NotEmpty
                                           String meetingAddress,

                                           String meetingAddressDetail,

                                           String meetingAddressDong) {

    public OfferingMeeting toOfferingMeeting() {
        return new OfferingMeeting(meetingDate, meetingAddress, meetingAddressDetail, meetingAddressDong);
    }
}
