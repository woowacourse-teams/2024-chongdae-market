package com.zzang.chongdae.offering.service.dto;

import com.zzang.chongdae.offering.domain.OfferingMeeting;
import java.time.LocalDateTime;

public record OfferingMeetingResponse(LocalDateTime deadline,
                                      String meetingAddress,
                                      String meetingAddressDetail,
                                      String meetingAddressDong) {

    public OfferingMeetingResponse(OfferingMeeting offeringMeeting) {
        this(offeringMeeting.getDeadline(),
                offeringMeeting.getMeetingAddress(),
                offeringMeeting.getMeetingAddressDetail(),
                offeringMeeting.getMeetingAddressDong()
        );
    }
}
