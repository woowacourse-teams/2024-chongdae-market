package com.zzang.chongdae.logging.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.zzang.chongdae.global.exception.ErrorMessage;
import com.zzang.chongdae.global.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LoggingErrorCode implements ErrorResponse {

    INVALID_STATUS(INTERNAL_SERVER_ERROR, "해당 상태는 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public ErrorMessage getErrorMessage() {
        return new ErrorMessage(this.message);
    }
}
