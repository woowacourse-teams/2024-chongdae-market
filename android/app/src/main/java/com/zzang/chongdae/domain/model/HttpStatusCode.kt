package com.zzang.chongdae.domain.model

enum class HttpStatusCode(val code: String) {
    OK_200("200"),
    NOT_FOUND_404("404"),
    UNAUTHORIZED_401("401"),
    CONFLICT_409("409"),
    INTERNAL_SERVER_ERROR_500("500"),
}
