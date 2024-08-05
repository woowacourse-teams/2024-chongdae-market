package com.zzang.chongdae.offering.domain.status;

import static com.zzang.chongdae.offering.domain.status.CommentRoomStatus.BUYING;
import static com.zzang.chongdae.offering.domain.status.CommentRoomStatus.TRADING;

public class Buying extends CommentRoomCondition {

    @Override
    public CommentRoomCondition nextCondition() {
        return TRADING.getCondition();
    }

    @Override
    public CommentRoomStatus status() {
        return BUYING;
    }
}
