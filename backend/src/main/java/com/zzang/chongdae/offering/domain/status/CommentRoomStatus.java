package com.zzang.chongdae.offering.domain.status;

import static com.zzang.chongdae.offering.exception.OfferingErrorCode.INVALID_CONDITION;

import com.zzang.chongdae.global.exception.MarketException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum CommentRoomStatus {

    GROUPING(new Grouping()),
    BUYING(new Buying()),
    TRADING(new Trading()),
    DONE(new Done());

    private final CommentRoomCondition condition; // TODO: refactoring - remove

    CommentRoomStatus(CommentRoomCondition condition) {
        this.condition = condition;
    }

    private static CommentRoomStatus toStatus(CommentRoomCondition condition) {
        return Arrays.stream(values())
                .filter(status -> status == condition.status())
                .findFirst()
                .orElseThrow(() -> new MarketException(INVALID_CONDITION));
    }

    public CommentRoomStatus nextStatus() {
        return toStatus(condition.nextCondition());
    }
}
