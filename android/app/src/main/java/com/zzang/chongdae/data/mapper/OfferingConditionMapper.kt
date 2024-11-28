package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.response.RemoteOfferingCondition
import com.zzang.chongdae.domain.model.OfferingCondition

fun RemoteOfferingCondition.toDomain(): OfferingCondition {
    return when (this) {
        RemoteOfferingCondition.FULL -> OfferingCondition.FULL
        RemoteOfferingCondition.TIME_OUT -> OfferingCondition.TIME_OUT
        RemoteOfferingCondition.CONFIRMED -> OfferingCondition.CONFIRMED
        RemoteOfferingCondition.AVAILABLE -> OfferingCondition.AVAILABLE
    }
}
