package com.zzang.chongdae.data.remote.mapper

import com.zzang.chongdae.data.remote.dto.request.OfferingModifyRequest
import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.domain.model.OfferingModify
import com.zzang.chongdae.domain.model.OfferingWrite

fun OfferingModify.toRequest(): OfferingModifyRequest {
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

fun OfferingWrite.toRequest(): OfferingWriteRequest {
    return OfferingWriteRequest(
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
