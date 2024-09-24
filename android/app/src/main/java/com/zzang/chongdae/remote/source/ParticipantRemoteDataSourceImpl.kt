package com.zzang.chongdae.remote.source

import com.zzang.chongdae.data.source.ParticipantRemoteDataSource
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.remote.api.ParticipationApiService
import com.zzang.chongdae.remote.dto.response.participants.ParticipantsResponse
import com.zzang.chongdae.remote.util.safeApiCall

class ParticipantRemoteDataSourceImpl(
    private val service: ParticipationApiService,
) : ParticipantRemoteDataSource {
    override suspend fun fetchParticipants(offeringId: Long): Result<ParticipantsResponse, DataError.Network> =
        safeApiCall {
            service.getParticipants(offeringId)
        }

    override suspend fun deleteParticipations(offeringId: Long): Result<Unit, DataError.Network> =
        safeApiCall {
            service.deleteParticipations(offeringId)
        }
}
