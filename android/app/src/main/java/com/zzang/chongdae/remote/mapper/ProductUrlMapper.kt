package com.zzang.chongdae.remote.mapper

import com.zzang.chongdae.data.remote.dto.request.ProductUrlRequest
import com.zzang.chongdae.data.remote.dto.response.offering.ProductUrlResponse
import com.zzang.chongdae.domain.model.ProductUrl

fun ProductUrlResponse.toDomain(): ProductUrl {
    return ProductUrl(
        imageUrl = this.imageUrl,
    )
}

fun String.toProductUrlRequest(): ProductUrlRequest {
    return ProductUrlRequest(
        productUrl = this,
    )
}
