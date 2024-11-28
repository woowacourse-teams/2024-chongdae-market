package com.zzang.chongdae.data.source

import com.zzang.chongdae.data.remote.dto.response.participants.ParticipantsResponse

interface ParticipantRemoteDataSource {
    suspend fun fetchParticipants(offeringId: Long): Result<ParticipantsResponse>
}
