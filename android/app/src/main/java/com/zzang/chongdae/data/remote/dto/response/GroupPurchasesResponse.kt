package com.zzang.chongdae.data.remote.dto.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupPurchasesResponse(
    @SerialName("responses") val responses: List<GroupPurchase>,
)
