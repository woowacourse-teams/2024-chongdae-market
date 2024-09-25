package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.api.ParticipationApiService
import com.zzang.chongdae.data.remote.dto.response.participants.ParticipantsResponse
import com.zzang.chongdae.data.remote.util.safeApiCall
import com.zzang.chongdae.data.source.ParticipantRemoteDataSource

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
