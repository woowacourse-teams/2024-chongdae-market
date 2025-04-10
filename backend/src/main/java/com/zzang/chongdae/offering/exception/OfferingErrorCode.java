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
    INVALID_CONDITION(BAD_REQUEST, "유효하지 않은 공모 상태입니다."),
    NOT_PARTICIPATE_MEMBER(BAD_REQUEST, "해당 공모의 참여자가 아닙니다."),
    NOT_PROPOSE_MEMBER(BAD_REQUEST, "해당 공모의 총대가 아닙니다."),
    CANNOT_DELETE_STATUS(BAD_REQUEST, "삭제할 수 없는 거래 상태입니다."),
    CANNOT_ORIGIN_PRICE_LESS_THAN_DIVIDED_PRICE(BAD_REQUEST, "원가 가격이 n빵 가격보다 작을 수 없습니다."),
    CANNOT_UPDATE_LESS_THAN_CURRENT_COUNT(BAD_REQUEST, "총 인원은 참여 인원수 미만으로 수정할 수 없습니다."),
    CANNOT_UPDATE_BEFORE_NOW_MEETING_DATE(BAD_REQUEST, "거래 날짜는 오늘보다 이전일 수 없습니다."),
    CANNOT_MEETING_DATE_BEFORE_THAN_TODAY(BAD_REQUEST, "거래 날짜는 오늘부터 설정할 수 있습니다."),
    INVALID_MY_COUNT(BAD_REQUEST, "총대의 구매 개수는 총 개수보다 적어야 합니다."),
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public ErrorMessage getErrorMessage() {
        return new ErrorMessage(this.message);
    }
}
