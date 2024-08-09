package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.response.OfferingStatusResponse
import com.zzang.chongdae.domain.model.OfferingStatus

fun OfferingStatusResponse.toDomain() =
    OfferingStatus(
        imageUrl = this.imageUrl,
        status = this.status,
        buttonText = this.buttonText,
    )
