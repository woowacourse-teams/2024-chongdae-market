package com.zzang.chongdae.remote.mapper

import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.remote.dto.response.offering.RemoteOffering

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
