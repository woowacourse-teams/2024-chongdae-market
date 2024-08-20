package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.api.ParticipationApiService
import com.zzang.chongdae.data.remote.dto.response.participants.ParticipantsResponse
import com.zzang.chongdae.data.source.ParticipantRemoteDataSource
import retrofit2.Response

class ParticipantRemoteDataSourceImpl(
    private val service: ParticipationApiService,
) : ParticipantRemoteDataSource {
    override suspend fun fetchParticipants(offeringId: Long): Result<ParticipantsResponse> =
        runCatching {
            val response: Response<ParticipantsResponse> =
                service.getParticipants(offeringId)
            if (response.isSuccessful) {
                response.body() ?: error(ERROR_PREFIX + ERROR_NULL_MESSAGE)
            } else {
                error(ERROR_PREFIX + response.code())
            }
        }

    override suspend fun deleteParticipations(offeringId: Long): Result<Unit> =
        runCatching {
            val response: Response<Unit> = service.deleteParticipations(offeringId)
            if (response.isSuccessful) {
                Unit
            } else {
                error(ERROR_PREFIX + response.code())
            }
        }

    companion object {
        const val ERROR_PREFIX = "에러 발생: "
        const val ERROR_NULL_MESSAGE = "null"
    }
}
