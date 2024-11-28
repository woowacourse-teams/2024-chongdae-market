package com.zzang.chongdae.storage.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.zzang.chongdae.global.exception.ErrorMessage;
import com.zzang.chongdae.global.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StorageErrorCode implements ErrorResponse {

    INVALID_FILE(BAD_REQUEST, "유효한 파일이 아닙니다."),
    INVALID_FILE_EXTENSION(BAD_REQUEST, "허용하지 않은 이미지 파일 확장자입니다."),
    STORAGE_SERVER_FAIL(INTERNAL_SERVER_ERROR, "이미지 서버에 문제가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public ErrorMessage getErrorMessage() {
        return new ErrorMessage(this.message);
    }
}
