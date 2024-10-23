package com.zzang.chongdae.notification.service.message;

import com.google.firebase.messaging.Message;
import com.zzang.chongdae.global.exception.MarketException;
import com.zzang.chongdae.notification.domain.FcmData;
import com.zzang.chongdae.notification.domain.FcmTopic;
import com.zzang.chongdae.notification.exception.NotificationErrorCode;
import com.zzang.chongdae.offering.domain.CommentRoomStatus;
import com.zzang.chongdae.offering.repository.entity.OfferingEntity;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RoomStatusMessageManager {

    private static final String MESSAGE_TYPE = "comment_detail";

    private final FcmMessageCreator messageCreator;

    public RoomStatusMessageManager() {
        this.messageCreator = FcmMessageCreator.getInstance();
    }

    public Message messageWhenUpdateStatus(OfferingEntity offering) {
        FcmTopic topic = FcmTopic.participantTopic(offering);
        FcmData data = new FcmData();
        data.addData("title", offering.getTitle());
        data.addData("body", CommentRoomStatusMapper.getView(offering.getRoomStatus()));
        data.addData("offering_id", offering.getId());
        data.addData("type", MESSAGE_TYPE);
        return messageCreator.createMessage(topic, data);
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
