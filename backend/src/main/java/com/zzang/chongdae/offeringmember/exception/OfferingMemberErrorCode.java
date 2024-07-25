package com.zzang.chongdae.offeringmember.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.zzang.chongdae.global.exception.ErrorMessage;
import com.zzang.chongdae.global.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OfferingMemberErrorCode implements ErrorResponse {

    DUPLICATED(BAD_REQUEST, "이미 참여한 공모엔 참여할 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public ErrorMessage getErrorMessage() {
        return new ErrorMessage(this.message);
    }
}
