package com.zzang.chongdae.offering.domain.status;

public abstract class CommentRoomCondition {

    public abstract CommentRoomCondition nextCondition();

    public abstract CommentRoomStatus status();
}
