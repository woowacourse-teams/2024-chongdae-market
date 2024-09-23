package com.zzang.chongdae.offering.domain;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UpdatedOffering {

    private final String title;
    private final String productUrl;
    private final String thumbnailUrl;
    private final OfferingPrice offeringPrice;
    private final String meetingAddress;
    private final String meetingAddressDetail;
    private final String meetingAddressDong;
    private final LocalDateTime meetingDate;
    private final String description;

    public UpdatedOffering(String title, String productUrl, String thumbnailUrl, Integer totalCount,
                           Integer totalPrice, Integer originPrice, String meetingAddress, String meetingAddressDetail,
                           String meetingAddressDong, LocalDateTime meetingDate, String description) {
        this.title = title;
        this.productUrl = productUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.offeringPrice = new OfferingPrice(totalCount, totalPrice, originPrice);
        this.meetingAddress = meetingAddress;
        this.meetingAddressDetail = meetingAddressDetail;
        this.meetingAddressDong = meetingAddressDong;
        this.meetingDate = meetingDate;
        this.description = description;
        validateMeetingDate();
    }

    private void validateMeetingDate() {
        LocalDateTime today = LocalDateTime.now();
        if (meetingDate.isBefore(today) || meetingDate.isEqual(today)) {
            throw new MarketException(OfferingErrorCode.CANNOT_UPDATE_BEFORE_NOW_MEETING_DATE);
        }
    }
}
