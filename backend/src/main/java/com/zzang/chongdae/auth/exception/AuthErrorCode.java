package com.zzang.chongdae.auth.exception;

import com.zzang.chongdae.global.exception.ErrorMessage;
import com.zzang.chongdae.global.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorResponse {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_PASSWORD(HttpStatus.NOT_FOUND, "가입하지 않은 회원입니다."),
    DUPLICATED_MEMBER(HttpStatus.CONFLICT, "이미 가입한 회원입니다."),
    CLIENT_TIME_OUT(HttpStatus.INTERNAL_SERVER_ERROR, "시간이 초과되어 로그인 요청에 실패했습니다."),
    KAKAO_LOGIN_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 로그인에 실패했습니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public ErrorMessage getErrorMessage() {
        return new ErrorMessage(this.message);
    }
}
