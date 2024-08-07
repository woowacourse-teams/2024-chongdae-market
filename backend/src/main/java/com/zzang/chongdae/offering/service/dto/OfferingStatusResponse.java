package com.zzang.chongdae.offering.service.dto;

import static com.zzang.chongdae.offering.domain.CommentRoomStatus.BUYING;
import static com.zzang.chongdae.offering.domain.CommentRoomStatus.DONE;
import static com.zzang.chongdae.offering.domain.CommentRoomStatus.GROUPING;
import static com.zzang.chongdae.offering.domain.CommentRoomStatus.TRADING;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.offering.domain.CommentRoomStatus;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import java.util.Arrays;

public record OfferingStatusResponse(CommentRoomStatus status,
                                     String imageUrl,
                                     String buttonText) {

    public OfferingStatusResponse(CommentRoomStatus status) {
        this(status, ViewMapper.toImage(status), ViewMapper.toButton(status));
    }

    private enum ViewMapper {

        GROUPING_VIEW(GROUPING, imageUrl("GROUPING"), "인원확정"),
        BUYING_VIEW(BUYING, imageUrl("BUYING"), "구매확정"),
        TRADING_VIEW(TRADING, imageUrl("TRADING"), "거래확정"),
        DONE_VIEW(DONE, imageUrl("DONE"), "거래완료");

        private final CommentRoomStatus roomStatus;
        private final String image;
        private final String button;

        ViewMapper(CommentRoomStatus roomStatus, String image, String button) {
            this.roomStatus = roomStatus;
            this.image = image;
            this.button = button;
        }

        private static String toImage(CommentRoomStatus status) {
            return Arrays.stream(values())
                    .filter(mapper -> mapper.roomStatus.equals(status))
                    .findFirst()
                    .orElseThrow(() -> new MarketException(OfferingErrorCode.INVALID_CONDITION))
                    .image;
        }

        private static String toButton(CommentRoomStatus status) {
            return Arrays.stream(values())
                    .filter(mapper -> mapper.roomStatus.equals(status))
                    .findFirst()
                    .orElseThrow(() -> new MarketException(OfferingErrorCode.INVALID_CONDITION))
                    .button;
        }

        private static String imageUrl(String status) {
            String imageUrlFormat = "https://d3a5rfnjdz82qu.cloudfront.net/chongdae-market/images/common/%s.png";
            return String.format(imageUrlFormat, status);
        }
    }
}
