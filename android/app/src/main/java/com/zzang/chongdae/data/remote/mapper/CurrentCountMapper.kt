package com.zzang.chongdae.data.remote.mapper

import com.zzang.chongdae.domain.model.CurrentCount

fun Int.toCurrentCount() = CurrentCount(this)
