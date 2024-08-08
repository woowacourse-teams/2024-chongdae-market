package com.zzang.chongdae.util

import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.FilterName
import com.zzang.chongdae.domain.model.FilterType
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.model.OfferingCondition

object TestFixture {
    val OFFERINGS_STUB =
        (0..20).map {
            Offering(
                id = it.toLong(),
                title = "",
                meetingAddress = "",
                thumbnailUrl = null,
                totalCount = 0,
                currentCount = 0,
                dividedPrice = 0,
                eachPrice = null,
                condition = OfferingCondition.CONFIRMED,
                isOpen = false,
            )
        }
    val FILTERS_STUB =
        (0..3).map {
            Filter(
                name = FilterName.HIGH_DISCOUNT,
                value = "",
                type = FilterType.VISIBLE,
            )
        }
}
