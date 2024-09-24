package com.zzang.chongdae.remote.dto.response.participants

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantsResponse(
    @SerialName("proposer") val remoteProposer: RemoteProposer,
    @SerialName("participants") val participants: List<RemoteParticipant>,
    @SerialName("count") val remoteCount: RemoteCount,
    @SerialName("price") val price: Int,
)
