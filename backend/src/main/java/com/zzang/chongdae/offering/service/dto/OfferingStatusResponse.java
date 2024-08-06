package com.zzang.chongdae.offering.service.dto;

import static com.zzang.chongdae.offering.domain.status.CommentRoomStatus.BUYING;
import static com.zzang.chongdae.offering.domain.status.CommentRoomStatus.DONE;
import static com.zzang.chongdae.offering.domain.status.CommentRoomStatus.GROUPING;
import static com.zzang.chongdae.offering.domain.status.CommentRoomStatus.TRADING;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.offering.domain.status.CommentRoomStatus;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import java.util.Arrays;
import java.util.List;

public record OfferingStatusResponse(String status,
                                     List<String> steps) {

    public OfferingStatusResponse(CommentRoomStatus commentRoomStatus) {
        this(commentRoomStatus.name(), OfferingStatusResponseMapper.toView(commentRoomStatus));
    }

    private enum OfferingStatusResponseMapper {

        GROUPING_VIEW(GROUPING, List.of("모집중", "인원확정", "구매중", "거래중")),
        BUYING_VIEW(BUYING, List.of("모집중", "인원확정", "구매중", "거래중")),
        TRADING_VIEW(TRADING, List.of("모집중", "인원확정", "구매완료", "거래중")),
        DONE_VIEW(DONE, List.of("모집중", "인원확정", "구매완료", "거래완료"));

        private final CommentRoomStatus roomStatus;
        private final List<String> view;

        OfferingStatusResponseMapper(CommentRoomStatus roomStatus, List<String> view) {
            this.roomStatus = roomStatus;
            this.view = view;
        }

        private static List<String> toView(CommentRoomStatus roomStatus) {
            return Arrays.stream(values())
                    .filter((value) -> value.roomStatus.equals(roomStatus))
                    .findFirst()
                    .orElseThrow(() -> new MarketException(OfferingErrorCode.INVALID_CONDITION))
                    .view;
        }
    }
}
