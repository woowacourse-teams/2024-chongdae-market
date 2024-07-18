package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.ParticipationResponse
import com.zzang.chongdae.domain.model.Participation

fun ParticipationResponse.toDomain() = Participation(
    status = this.status.toDomain(),
    this.currentCount.toCurrentCount()
)
