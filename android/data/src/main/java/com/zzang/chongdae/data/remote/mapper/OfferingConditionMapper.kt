package com.zzang.chongdae.data.remote.mapper

import com.zzang.chongdae.data.remote.dto.response.offering.RemoteOfferingStatus
import com.zzang.chongdae.domain.model.participant.OfferingCondition

fun com.zzang.chongdae.data.remote.dto.response.offering.RemoteOfferingStatus.toDomain(): OfferingCondition {
    return when (this) {
        com.zzang.chongdae.data.remote.dto.response.offering.RemoteOfferingStatus.FULL -> OfferingCondition.FULL
        com.zzang.chongdae.data.remote.dto.response.offering.RemoteOfferingStatus.IMMINENT -> OfferingCondition.IMMINENT
        com.zzang.chongdae.data.remote.dto.response.offering.RemoteOfferingStatus.CONFIRMED -> OfferingCondition.CONFIRMED
        com.zzang.chongdae.data.remote.dto.response.offering.RemoteOfferingStatus.AVAILABLE -> OfferingCondition.AVAILABLE
    }
}
