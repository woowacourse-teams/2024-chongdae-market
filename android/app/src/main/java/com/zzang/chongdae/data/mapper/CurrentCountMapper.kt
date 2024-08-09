package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.domain.model.CurrentCount

fun Int.toCurrentCount() = CurrentCount(this)
