package com.zzang.chongdae.data.remote.mapper

import com.zzang.chongdae.data.remote.dto.request.ProductUrlRequest
import com.zzang.chongdae.data.remote.dto.response.offering.ProductUrlResponse
import com.zzang.chongdae.domain.model.offeringwrite.ProductUrl

fun com.zzang.chongdae.data.remote.dto.response.offering.ProductUrlResponse.toDomain(): ProductUrl {
    return ProductUrl(
        imageUrl = this.imageUrl,
    )
}

fun String.toProductUrlRequest(): com.zzang.chongdae.data.remote.dto.request.ProductUrlRequest {
    return com.zzang.chongdae.data.remote.dto.request.ProductUrlRequest(
        productUrl = this,
    )
}
