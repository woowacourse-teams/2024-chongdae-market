package com.zzang.chongdae.offering.domain.status;

import static com.zzang.chongdae.offering.domain.status.CommentRoomStatus.BUYING;
import static com.zzang.chongdae.offering.domain.status.CommentRoomStatus.GROUPING;

public class Grouping extends CommentRoomCondition {

    @Override
    public CommentRoomCondition nextCondition() {
        return BUYING.getCondition();
    }

    @Override
    public CommentRoomStatus status() {
        return GROUPING;
    }
}
