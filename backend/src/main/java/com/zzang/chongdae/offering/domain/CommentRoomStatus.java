package com.zzang.chongdae.offering.domain;

import lombok.Getter;

@Getter
public enum CommentRoomStatus {

    GROUPING,
    BUYING,
    TRADING,
    DONE;

    static {
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
