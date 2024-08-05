package com.zzang.chongdae.offering.domain.status;

public abstract class CommentRoomCondition {

    abstract public CommentRoomCondition nextCondition();

    abstract public CommentRoomStatus status();
}
