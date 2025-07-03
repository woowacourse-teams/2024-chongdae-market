@file:Suppress("UNUSED_EXPRESSION")

package com.zzang.chongdae.data.remote.mapper

import com.zzang.chongdae.data.remote.dto.response.offering.RemoteFilter
import com.zzang.chongdae.data.remote.dto.response.offering.RemoteFilterName
import com.zzang.chongdae.data.remote.dto.response.offering.RemoteFilterType
import com.zzang.chongdae.domain.model.offering.Filter
import com.zzang.chongdae.domain.model.offering.FilterName
import com.zzang.chongdae.domain.model.offering.FilterType

fun com.zzang.chongdae.data.remote.dto.response.offering.RemoteFilter.toDomain() =
    Filter(
        name = this.name.toDomain(),
        value = this.value,
        type = this.type.toDomain(),
    )

fun com.zzang.chongdae.data.remote.dto.response.offering.RemoteFilterName.toDomain(): FilterName {
    return when (this) {
        com.zzang.chongdae.data.remote.dto.response.offering.RemoteFilterName.JOINABLE -> FilterName.JOINABLE
        com.zzang.chongdae.data.remote.dto.response.offering.RemoteFilterName.IMMINENT -> FilterName.IMMINENT
        com.zzang.chongdae.data.remote.dto.response.offering.RemoteFilterName.HIGH_DISCOUNT -> FilterName.HIGH_DISCOUNT
        com.zzang.chongdae.data.remote.dto.response.offering.RemoteFilterName.RECENT -> FilterName.RECENT
    }
}

fun com.zzang.chongdae.data.remote.dto.response.offering.RemoteFilterType.toDomain(): FilterType {
    return when (this) {
        com.zzang.chongdae.data.remote.dto.response.offering.RemoteFilterType.VISIBLE -> FilterType.VISIBLE
        com.zzang.chongdae.data.remote.dto.response.offering.RemoteFilterType.INVISIBLE -> FilterType.INVISIBLE
    }
}
