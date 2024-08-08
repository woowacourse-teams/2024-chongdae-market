package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.response.RemoteOffering
import com.zzang.chongdae.domain.model.Offering

fun RemoteOffering.toDomain() =
    Offering(
        id = this.id,
        title = this.title,
        meetingAddress = this.meetingAddress ?: "",
        thumbnailUrl = this.thumbnailUrl,
        totalCount = this.totalCount,
        currentCount = this.currentCount,
        dividedPrice = this.dividedPrice,
        eachPrice = this.originPrice,
        condition = this.condition.toDomain(),
        isOpen = this.isOpen,
    )
