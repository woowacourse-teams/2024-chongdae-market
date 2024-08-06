package com.zzang.chongdae.offering.domain.status;

import static com.zzang.chongdae.offering.domain.status.CommentRoomStatus.DONE;
import static com.zzang.chongdae.offering.domain.status.CommentRoomStatus.TRADING;

public class Trading extends CommentRoomCondition {

    @Override
    public CommentRoomCondition nextCondition() {
        return DONE.getCondition();
    }

    @Override
    public CommentRoomStatus status() {
        return TRADING;
    }
}
