package com.zzang.chongdae.data.remote.dto


import kotlinx.serialization.Serializable

@Serializable
data class GroupPurchasesResponse(
    val responses: List<GroupPurchase>,
)
