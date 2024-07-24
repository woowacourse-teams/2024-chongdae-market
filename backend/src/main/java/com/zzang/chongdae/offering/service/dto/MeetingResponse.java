package com.zzang.chongdae.offering.service.dto;

import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.time.LocalDateTime;

public record MeetingResponse(LocalDateTime deadline,
                              String meetingAddress,
                              String meetingAddressDetail) {

    public MeetingResponse(OfferingEntity offering) {
        this(offering.getDeadline(), offering.getMeetingAddress(), offering.getMeetingAddressDetail());
    }
}
