package com.zzang.chongdae.offering.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.zzang.chongdae.global.exception.ErrorMessage;
import com.zzang.chongdae.global.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OfferingErrorCode implements ErrorResponse {

    NOT_FOUND(BAD_REQUEST, "해당 공모가 존재하지 않습니다."),
    NOT_FOUND_FILTER(BAD_REQUEST, "해당 필터가 존재하지 않습니다."),
    NOT_SUPPORTED_FILTER(BAD_REQUEST, "현재는 지원하지 않는 필터입니다."),
    PARTICIPANT_FULL(BAD_REQUEST, "해당 공모에 참여 가능한 인원수를 초과하였습니다."),
    CANNOT_PARTICIPATE(BAD_REQUEST, "참여할 수 없는 공모입니다."),
    INVALID_CONDITION(BAD_REQUEST, "유효하지 않은 공모 상태입니다");

    private final HttpStatus status;
    private final String message;

    @Override
    public ErrorMessage getErrorMessage() {
        return new ErrorMessage(this.message);
    }
}
