package com.zzang.chongdae.notification.exception;

import com.zzang.chongdae.global.exception.ErrorMessage;
import com.zzang.chongdae.global.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NotificationErrorCode implements ErrorResponse {

    CANNOT_SEND_ALARM(HttpStatus.INTERNAL_SERVER_ERROR, "FCM 알림 전송에 실패하였습니다."),
    CANNOT_FIND_URL(HttpStatus.INTERNAL_SERVER_ERROR, "해당 URL을 찾을 수 없습니다."),
    INVALID_COMMENT_ROOM_STATUS(HttpStatus.INTERNAL_SERVER_ERROR, "존재하지 않는 거래 상태입니다."),
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public ErrorMessage getErrorMessage() {
        return new ErrorMessage(this.message);
    }
}
