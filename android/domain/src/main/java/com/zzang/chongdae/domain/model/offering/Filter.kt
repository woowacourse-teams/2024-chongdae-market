package com.zzang.chongdae.domain.model.offering

data class Filter(
    val name: FilterName,
    val value: String,
    val type: FilterType,
)
