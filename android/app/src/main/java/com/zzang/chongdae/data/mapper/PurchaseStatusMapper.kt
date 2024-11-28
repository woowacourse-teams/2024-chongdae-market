package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.response.PurchaseStatus
import com.zzang.chongdae.domain.model.ArticleStatus

fun PurchaseStatus.toDomain(): ArticleStatus {
    return when (this) {
        PurchaseStatus.FULL -> ArticleStatus.FULL
        PurchaseStatus.TIME_OUT -> ArticleStatus.TIME_OUT
        PurchaseStatus.CONFIRMED -> ArticleStatus.CONFIRMED
        PurchaseStatus.AVAILABLE -> ArticleStatus.AVAILABLE
    }
}
