package com.zzang.chongdae.remote.mapper

import com.zzang.chongdae.domain.model.Participation
import com.zzang.chongdae.remote.dto.response.offering.ParticipationResponse

fun ParticipationResponse.toDomain() =
    Participation(
        offeringCondition = this.offeringCondition.toDomain(),
        currentCount = this.currentCount.toCurrentCount(),
    )
