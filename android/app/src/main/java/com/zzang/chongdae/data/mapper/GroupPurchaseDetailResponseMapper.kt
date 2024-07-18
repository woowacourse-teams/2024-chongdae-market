package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.GroupPurchaseDetailResponse
import com.zzang.chongdae.domain.model.ArticleDetail
import com.zzang.chongdae.domain.model.CurrentCount
import com.zzang.chongdae.domain.model.Participation
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

fun String.toLocalDateTime(): LocalDateTime =
    LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

fun Int.toCurrentCount() = CurrentCount(this)

