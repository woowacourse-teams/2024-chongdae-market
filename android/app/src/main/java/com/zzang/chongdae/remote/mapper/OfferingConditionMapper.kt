package com.zzang.chongdae.remote.mapper

import com.zzang.chongdae.domain.model.OfferingCondition
import com.zzang.chongdae.remote.dto.response.offering.RemoteOfferingStatus

fun RemoteOfferingStatus.toDomain(): OfferingCondition {
    return when (this) {
        RemoteOfferingStatus.FULL -> OfferingCondition.FULL
        RemoteOfferingStatus.IMMINENT -> OfferingCondition.IMMINENT
        RemoteOfferingStatus.CONFIRMED -> OfferingCondition.CONFIRMED
        RemoteOfferingStatus.AVAILABLE -> OfferingCondition.AVAILABLE
    }
}
