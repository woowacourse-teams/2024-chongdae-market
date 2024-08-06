package com.zzang.chongdae.offering.domain.status;

import static com.zzang.chongdae.offering.domain.status.CommentRoomStatus.DONE;

public class Done extends CommentRoomCondition {

    @Override
    public CommentRoomCondition nextCondition() {
        return DONE.getCondition();
    }

    @Override
    public CommentRoomStatus status() {
        return DONE;
    }
}
