package com.zzang.chongdae.data.source

import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.remote.dto.response.participants.ParticipantsResponse

interface ParticipantRemoteDataSource {
    suspend fun fetchParticipants(offeringId: Long): Result<ParticipantsResponse, DataError.Network>

    suspend fun deleteParticipations(offeringId: Long): Result<Unit, DataError.Network>
}
