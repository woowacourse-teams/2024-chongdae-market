package com.zzang.chongdae.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MarketException extends RuntimeException {

    private final ErrorResponse errorResponse;
}
