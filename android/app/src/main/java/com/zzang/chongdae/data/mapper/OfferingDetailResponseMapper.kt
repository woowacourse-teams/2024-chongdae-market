package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.response.offering.OfferingDetailResponse
import com.zzang.chongdae.domain.model.OfferingDetail

fun OfferingDetailResponse.toDomain() =
    OfferingDetail(
        id = this.id,
        title = this.title,
        nickname = this.nickname,
        isProposer = this.isProposer,
        productUrl = this.productUrl,
        dividedPrice = this.dividedPrice,
        thumbnailUrl = this.thumbnailUrl,
        totalPrice = this.totalPrice,
        meetingDate = this.meetingDate.toLocalDateTime(),
        currentCount = this.currentCount.toCurrentCount(),
        totalCount = this.totalCount,
        meetingAddress = this.meetingAddress,
        meetingAddressDetail = this.meetingAddressDetail,
        description = this.description,
        condition = this.condition.toDomain(),
        isParticipated = this.isParticipated,
    )
