package com.zzang.chongdae.data.remote.mapper

import com.zzang.chongdae.data.remote.dto.request.OfferingModifyRequest
import com.zzang.chongdae.data.remote.dto.response.offering.OfferingModifyResponse
import com.zzang.chongdae.domain.model.OfferingModifyDomainRequest
import com.zzang.chongdae.domain.model.OfferingModifyDomainResponse

fun OfferingModifyDomainRequest.toRequest(): OfferingModifyRequest {
    return OfferingModifyRequest(
        title = this.title,
        productUrl = this.productUrl,
        thumbnailUrl = this.thumbnailUrl,
        totalCount = this.totalCount,
        totalPrice = this.totalPrice,
        originPrice = this.originPrice,
        meetingAddress = this.meetingAddress,
        meetingAddressDong = this.meetingAddressDong,
        meetingAddressDetail = this.meetingAddressDetail,
        meetingDate = this.meetingDate,
        description = this.description,
    )
}

fun OfferingModifyResponse.toDomain(): OfferingModifyDomainResponse {
    return OfferingModifyDomainResponse(
        id = this.id,
        title = this.title,
        productUrl = this.productUrl,
        meetingAddress = this.meetingAddress,
        meetingAddressDetail = this.meetingAddressDetail,
        description = this.description,
        meetingDate = this.meetingDate.toLocalDateTime(),
        currentCount = this.currentCount.toCurrentCount(),
        totalCount = this.totalCount,
        thumbnailUrl = this.thumbnailUrl,
        dividedPrice = this.dividedPrice,
        totalPrice = this.totalPrice,
        condition = this.condition.toDomain(),
        nickname = this.nickname,
    )
}