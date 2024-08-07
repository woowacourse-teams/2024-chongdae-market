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
                                     String imageUrl) {

    public OfferingStatusResponse(CommentRoomStatus commentRoomStatus) {
        this(commentRoomStatus, ImageMapper.toImage(commentRoomStatus));
    }

    private enum ImageMapper {

        GROUPING_IMAGE(GROUPING, imageUrl("GROUPING")),
        BUYING_IMAGE(BUYING, imageUrl("BUYING")),
        TRADING_IMAGE(TRADING, imageUrl("TRADING")),
        DONE_IMAGE(DONE, imageUrl("DONE"));

        private final CommentRoomStatus roomStatus;
        private final String image;

        ImageMapper(CommentRoomStatus roomStatus, String image) {
            this.roomStatus = roomStatus;
            this.image = image;
        }

        private static String toImage(CommentRoomStatus roomStatus) {
            return Arrays.stream(values())
                    .filter(mapper -> mapper.roomStatus.equals(roomStatus))
                    .findFirst()
                    .orElseThrow(() -> new MarketException(OfferingErrorCode.INVALID_CONDITION))
                    .image;
        }

        private static String imageUrl(String status) {
            String imageUrlFormat = "https://d3a5rfnjdz82qu.cloudfront.net/chongdae-market/images/common/%s.png";
            return String.format(imageUrlFormat, status);
        }
    }
}
