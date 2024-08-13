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

    DUPLICATED(BAD_REQUEST, "이미 참여한 공모엔 참여할 수 없습니다."),
    OFFERING_NOT_FOUND(BAD_REQUEST, "참여 공모가 존재하지 않습니다."),
    PARTICIPANT_NOT_FOUND(BAD_REQUEST, "참여하지 않은 공모입니다."),
    PROPOSER_NOT_FOUND(BAD_REQUEST, "총대를 찾을 수 없습니다."),
    CANNOT_CANCEL_PROPOSER(BAD_REQUEST, "해당 공모의 총대는 참여를 취소할 수 없습니다."),
    CANNOT_CANCEL_IN_PROGRESS(BAD_REQUEST, "진행중인 공모는 참여를 취소할 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public ErrorMessage getErrorMessage() {
        return new ErrorMessage(this.message);
    }
}
