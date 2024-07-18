package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.GroupPurchase
import com.zzang.chongdae.domain.model.Article


fun GroupPurchase.toDomain() = Article(
    id = this.id,
    thumbnailUrl = this.thumbnailUrl,
    deadline = this.deadline.toLocalDateTime(),
    title = this.title,
    meetingAddress = this.meetingAddress,
    splitPrice = this.dividedPrice,
    totalCount = this.totalCount,
    currentCount = this.currentCount,
    isClosed = this.isClosed
)

