package com.zzang.chongdae.comment.service.dto;

import static com.zzang.chongdae.offering.domain.CommentRoomStatus.BUYING;
import static com.zzang.chongdae.offering.domain.CommentRoomStatus.DONE;
import static com.zzang.chongdae.offering.domain.CommentRoomStatus.GROUPING;
import static com.zzang.chongdae.offering.domain.CommentRoomStatus.TRADING;

import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.member.repository.entity.MemberEntity;
import com.zzang.chongdae.offering.domain.CommentRoomStatus;
import com.zzang.chongdae.offering.exception.OfferingErrorCode;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.Arrays;

public record CommentRoomInfoResponse(CommentRoomStatus status,
                                      String imageUrl,
                                      String buttonText,
                                      String message,
                                      String title,
                                      boolean isProposer) {

    public CommentRoomInfoResponse(OfferingEntity offering, MemberEntity member) {
        this(offering.getRoomStatus(),
                ViewMapper.toImage(offering.getRoomStatus()),
                ViewMapper.toButton(offering.getRoomStatus()),
                ViewMapper.toMessage(offering.getRoomStatus()),
                offering.getTitle(),
                offering.isProposedBy(member));
    }

    private enum ViewMapper {

        GROUPING_VIEW(GROUPING, imageUrl("GROUPING"), "인원확정", "공동구매에 참여할 인원이\n모이면 인원을 확정하세요."),
        BUYING_VIEW(BUYING, imageUrl("BUYING"), "구매확정", "총대가 물품 구매를 완료하면 확정하세요."),
        TRADING_VIEW(TRADING, imageUrl("TRADING"), "거래확정", "총대가 참여자들과\n거래를 완료하면 확정하세요."),
        DONE_VIEW(DONE, imageUrl("DONE"), "거래완료", "거래가 완료되었어요.");

        private final CommentRoomStatus roomStatus;
        private final String image;
        private final String button;
        private final String message;

        ViewMapper(CommentRoomStatus roomStatus, String image, String button, String message) {
            this.roomStatus = roomStatus;
            this.image = image;
            this.button = button;
            this.message = message;
        }

        private static String toImage(CommentRoomStatus status) {
            return findViewMapper(status).image;
        }

        private static String toButton(CommentRoomStatus status) {
            return findViewMapper(status).button;
        }

        private static String toMessage(CommentRoomStatus status) {
            return findViewMapper(status).message;
        }

        private static ViewMapper findViewMapper(CommentRoomStatus status) {
            return Arrays.stream(values())
                    .filter(mapper -> mapper.roomStatus.equals(status))
                    .findFirst()
                    .orElseThrow(() -> new MarketException(OfferingErrorCode.INVALID_CONDITION));
        }

        private static String imageUrl(String status) {
            String imageUrlFormat = "https://d3a5rfnjdz82qu.cloudfront.net/chongdae-market/images/common/%s.png";
            return String.format(imageUrlFormat, status);
        }
    }
}
