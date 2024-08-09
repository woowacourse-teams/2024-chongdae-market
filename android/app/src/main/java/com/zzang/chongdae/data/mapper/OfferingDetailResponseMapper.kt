package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.response.OfferingDetailResponse
import com.zzang.chongdae.domain.model.OfferingDetail

fun OfferingDetailResponse.toDomain() =
    OfferingDetail(
        id = this.id,
        title = this.title,
        nickname = this.nickname,
        memberId = this.memberId.toString(),
        productUrl = this.productUrl,
        dividedPrice = this.dividedPrice,
        thumbnailUrl = this.thumbnailUrl,
        totalPrice = this.totalPrice,
        deadline = this.deadline.toLocalDateTime(),
        currentCount = this.currentCount.toCurrentCount(),
        totalCount = this.totalCount,
        meetingAddress = this.meetingAddress,
        meetingAddressDetail = this.meetingAddressDetail,
        description = this.description,
        condition = this.condition.toDomain(),
        isParticipated = this.isParticipated,
    )
