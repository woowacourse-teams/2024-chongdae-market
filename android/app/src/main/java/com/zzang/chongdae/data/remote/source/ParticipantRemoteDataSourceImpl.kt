package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.api.ParticipationApiService
import com.zzang.chongdae.data.remote.dto.response.participants.ParticipantsResponse
import com.zzang.chongdae.data.remote.util.safeApiCall
import com.zzang.chongdae.data.source.ParticipantRemoteDataSource
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

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
