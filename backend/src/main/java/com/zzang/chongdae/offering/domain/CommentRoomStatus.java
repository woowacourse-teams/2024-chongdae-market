package com.zzang.chongdae.offering.domain;

import lombok.Getter;

@Getter
public enum CommentRoomStatus {

    DELETED,
    GROUPING,
    BUYING,
    TRADING,
    DONE;

    static {
        DELETED.nextStatus = DELETED;
        GROUPING.nextStatus = BUYING;
        BUYING.nextStatus = TRADING;
        TRADING.nextStatus = DONE;
        DONE.nextStatus = DONE;
    }

    private CommentRoomStatus nextStatus;

    public CommentRoomStatus nextStatus() {
        return this.nextStatus;
    }

    public boolean isGrouping() {
        return this == GROUPING;
    }

    public boolean isBuying() {
        return this == BUYING;
    }

    public boolean isGrouped() {
        return this != GROUPING;
    }

    public boolean isInProgress() {
        return this == BUYING || this == TRADING;
    }
}
