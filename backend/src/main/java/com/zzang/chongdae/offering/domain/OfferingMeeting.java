package com.zzang.chongdae.offering.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OfferingMeeting {

    private final LocalDateTime deadline;
    private final String meetingAddress;
    private final String meetingAddressDetail;
    private final String meetingAddressDong;
}
