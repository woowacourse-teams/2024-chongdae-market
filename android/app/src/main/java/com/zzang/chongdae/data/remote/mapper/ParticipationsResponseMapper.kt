package com.zzang.chongdae.data.remote.mapper

import com.zzang.chongdae.data.remote.dto.response.offering.ParticipationResponse
import com.zzang.chongdae.domain.model.Participation

fun ParticipationResponse.toDomain() =
    Participation(
        offeringCondition = this.offeringCondition.toDomain(),
        currentCount = this.currentCount.toCurrentCount(),
    )
