package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.response.OfferingCondition
import com.zzang.chongdae.domain.model.ArticleStatus

fun OfferingCondition.toDomain(): ArticleStatus {
    return when (this) {
        OfferingCondition.FULL -> ArticleStatus.FULL
        OfferingCondition.TIME_OUT -> ArticleStatus.TIME_OUT
        OfferingCondition.CONFIRMED -> ArticleStatus.CONFIRMED
        OfferingCondition.AVAILABLE -> ArticleStatus.AVAILABLE
    }
}
