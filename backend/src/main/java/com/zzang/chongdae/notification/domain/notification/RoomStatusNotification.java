package com.zzang.chongdae.notification.domain.notification;

import com.google.firebase.messaging.Message;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.notification.domain.FcmCondition;
import com.zzang.chongdae.notification.domain.FcmData;
import com.zzang.chongdae.notification.exception.NotificationErrorCode;
import com.zzang.chongdae.notification.service.FcmMessageManager;
import com.zzang.chongdae.offering.domain.CommentRoomStatus;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RoomStatusNotification {

    private final FcmMessageManager messageManager;
    private final OfferingEntity offering;

    public Message messageWhenUpdateStatus() {
        FcmCondition condition = FcmCondition.roomStatusCondition(offering);
        FcmData data = new FcmData();
        data.addData("title", offering.getTitle());
        data.addData("body", CommentRoomStatusMapper.getView(offering.getRoomStatus()));
        data.addData("offering_id", offering.getId());
        data.addData("type", "comment_detail");
        return messageManager.createMessage(condition, data);
    }

    private enum CommentRoomStatusMapper {

        DELETED(CommentRoomStatus.DELETED, ""),
        GROUPING(CommentRoomStatus.GROUPING, ""),
        BUYING(CommentRoomStatus.BUYING, "모집이 마감됐어요! 이제 총대가 물건을 구매할 거예요"),
        TRADING(CommentRoomStatus.TRADING, "총대가 물건을 구매했어요! 이제 거래를 진행해보아요"),
        DONE(CommentRoomStatus.DONE, "거래가 완료되었어요. 고마워요 :)"),
        ;

        private final CommentRoomStatus status;
        private final String view;

        CommentRoomStatusMapper(CommentRoomStatus status, String view) {
            this.status = status;
            this.view = view;
        }

        public static String getView(CommentRoomStatus roomStatus) {
            return Arrays.stream(values())
                    .filter(v -> v.status.equals(roomStatus))
                    .findAny()
                    .orElseThrow(() -> new MarketException(NotificationErrorCode.INVALID_COMMENT_ROOM_STATUS))
                    .view;
        }
    }
}
