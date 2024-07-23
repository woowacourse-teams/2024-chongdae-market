package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.domain.model.OfferingCondition

fun OfferingCondition.toDomain(): OfferingCondition {
    return when (this) {
        OfferingCondition.FULL -> OfferingCondition.FULL
        OfferingCondition.TIME_OUT -> OfferingCondition.TIME_OUT
        OfferingCondition.CONFIRMED -> OfferingCondition.CONFIRMED
        OfferingCondition.AVAILABLE -> OfferingCondition.AVAILABLE
    }
}
