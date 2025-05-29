package com.zzang.chongdae.data.remote.mapper.analytics

import com.zzang.chongdae.data.remote.dto.response.analytics.UserTypeResponse
import com.zzang.chongdae.domain.model.analytics.UserType

fun UserTypeResponse.toDomain(): UserType {
    return UserType(
        typeName = this.type,
    )
}
