package com.zzang.chongdae.remote.mapper

import com.zzang.chongdae.data.remote.dto.response.offering.RemoteOffering
import com.zzang.chongdae.domain.model.Offering

fun RemoteOffering.toDomain() =
    Offering(
        id = this.id,
        title = this.title,
        meetingAddressDong = this.meetingAddressDong ?: "",
        thumbnailUrl = this.thumbnailUrl,
        totalCount = this.totalCount,
        currentCount = this.currentCount,
        dividedPrice = this.dividedPrice,
        originPrice = this.originPrice,
        status = this.status.toDomain(),
        isOpen = this.isOpen,
    )
