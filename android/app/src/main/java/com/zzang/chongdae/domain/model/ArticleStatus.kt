package com.zzang.chongdae.domain.model

enum class ArticleStatus {
    FULL,
    TIME_OUT,
    CONFIRMED,
    AVAILABLE, ;

    companion object {
        fun ArticleStatus.isAvailable(): Boolean {
            return this == AVAILABLE
        }
    }
}
