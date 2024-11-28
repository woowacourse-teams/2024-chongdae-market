package com.zzang.chongdae.notification.exception;

import com.zzang.chongdae.global.exception.ErrorMessage;
import com.zzang.chongdae.global.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NotificationErrorCode implements ErrorResponse {

    INVALID_COMMENT_ROOM_STATUS(HttpStatus.INTERNAL_SERVER_ERROR, "존재하지 않는 거래 상태입니다."),
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public ErrorMessage getErrorMessage() {
        return new ErrorMessage(this.message);
    }
}
