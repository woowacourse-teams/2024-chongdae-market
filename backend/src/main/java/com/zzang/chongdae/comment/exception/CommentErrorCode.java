package com.zzang.chongdae.comment.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.zzang.chongdae.global.exception.ErrorMessage;
import com.zzang.chongdae.global.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements ErrorResponse {

    NOT_FOUND(BAD_REQUEST, "해당 댓글이 존재하지 않습니다."),
    NOT_PROPOSER(BAD_REQUEST, "해당 권한은 총대에게만 부여됩니다."),
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public ErrorMessage getErrorMessage() {
        return new ErrorMessage(this.message);
    }
}
