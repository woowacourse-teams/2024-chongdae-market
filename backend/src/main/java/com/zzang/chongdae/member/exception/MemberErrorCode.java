package com.zzang.chongdae.member.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.zzang.chongdae.global.exception.ErrorMessage;
import com.zzang.chongdae.global.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorResponse {

    NOT_FOUND(BAD_REQUEST, "해당 사용자가 존재하지 않습니다."),
    MAX_TRY_EXCEEDED(INTERNAL_SERVER_ERROR, "닉네임 생성에 실패했습니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public ErrorMessage getErrorMessage() {
        return new ErrorMessage(this.message);
    }
}
