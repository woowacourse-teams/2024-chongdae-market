package com.zzang.chongdae.remote.mapper

import com.zzang.chongdae.domain.model.ProductUrl
import com.zzang.chongdae.remote.dto.request.ProductUrlRequest
import com.zzang.chongdae.remote.dto.response.offering.ProductUrlResponse

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
