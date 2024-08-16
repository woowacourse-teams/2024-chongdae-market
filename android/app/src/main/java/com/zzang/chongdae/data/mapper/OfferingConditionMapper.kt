package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.response.RemoteOfferingStatus
import com.zzang.chongdae.domain.model.OfferingCondition

fun RemoteOfferingStatus.toDomain(): OfferingCondition {
    return when (this) {
        RemoteOfferingStatus.FULL -> OfferingCondition.FULL
        RemoteOfferingStatus.IMMINENT -> OfferingCondition.IMMINENT
        RemoteOfferingStatus.CONFIRMED -> OfferingCondition.CONFIRMED
        RemoteOfferingStatus.AVAILABLE -> OfferingCondition.AVAILABLE
    }
}
