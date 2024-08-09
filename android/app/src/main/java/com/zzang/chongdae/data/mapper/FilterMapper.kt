@file:Suppress("UNUSED_EXPRESSION")

package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.response.RemoteFilter
import com.zzang.chongdae.data.remote.dto.response.RemoteFilterName
import com.zzang.chongdae.data.remote.dto.response.RemoteFilterType
import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.FilterName
import com.zzang.chongdae.domain.model.FilterType

fun RemoteFilter.toDomain() =
    Filter(
        name = this.name.toDomain(),
        value = this.value,
        type = this.type.toDomain(),
    )

fun RemoteFilterName.toDomain(): FilterName {
    return when (this) {
        RemoteFilterName.JOINABLE -> FilterName.JOINABLE
        RemoteFilterName.IMMINENT -> FilterName.IMMINENT
        RemoteFilterName.HIGH_DISCOUNT -> FilterName.HIGH_DISCOUNT
        RemoteFilterName.RECENT -> FilterName.RECENT
    }
}

fun RemoteFilterType.toDomain(): FilterType {
    return when (this) {
        RemoteFilterType.VISIBLE -> FilterType.VISIBLE
        RemoteFilterType.INVISIBLE -> FilterType.INVISIBLE
    }
}
