package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.response.GroupPurchaseDetailResponse
import com.zzang.chongdae.domain.model.ArticleDetail

fun GroupPurchaseDetailResponse.toDomain() =
    ArticleDetail(
        id = this.id,
        title = this.title,
        nickname = this.nickname,
        productUrl = this.productUrl,
        splitPrice = this.dividedPrice,
        thumbnailUrl = this.thumbnailUrl,
        totalPrice = this.totalPrice,
        dueDateTime = this.deadline.toLocalDateTime(),
        currentCount = this.currentCount.toCurrentCount(),
        totalCount = this.totalCount,
        meetingAddress = this.meetingAddress,
        meetingAddressDetail = this.meetingAddressDetail,
        description = this.description,
        status = this.status.toDomain(),
    )
