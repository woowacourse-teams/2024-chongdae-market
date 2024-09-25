package com.zzang.chongdae.common.handler

sealed interface DataError : Error {
    enum class Network : DataError {
        UNAUTHORIZED,
        UNKNOWN,
        NULL,
        CONNECTION_ERROR,
        FORBIDDEN,
        NOT_FOUND,
        SERVER_ERROR,
        BAD_REQUEST,
        CONFLICT,
        FAIL_REFRESH,
    }
}
