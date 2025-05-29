package com.zzang.chongdae.domain.model.participant

import com.zzang.chongdae.domain.model.CurrentCount
import com.zzang.chongdae.domain.model.OfferingCondition

data class Participation(
    val offeringCondition: OfferingCondition,
    val currentCount: CurrentCount,
)
