package com.zzang.chongdae.data.remote.dto.response.participants

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantsResponse(
    @SerialName("proposer") val remoteProposer: com.zzang.chongdae.data.remote.dto.response.participants.RemoteProposer,
    @SerialName("participants") val participants: List<com.zzang.chongdae.data.remote.dto.response.participants.RemoteParticipant>,
    @SerialName("count") val remoteCount: com.zzang.chongdae.data.remote.dto.response.participants.RemoteCount,
    @SerialName("price") val price: Int,
)
