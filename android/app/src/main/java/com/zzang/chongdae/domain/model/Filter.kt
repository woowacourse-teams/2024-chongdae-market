package com.zzang.chongdae.domain.model

data class Filter(
    val name: FilterName,
    val value: String,
    val type: FilterType,
)
