package com.zzang.chongdae.data.repository

import com.zzang.chongdae.data.mapper.participant.toDomain
import com.zzang.chongdae.data.source.ParticipantRemoteDataSource
import com.zzang.chongdae.domain.model.participant.Participants
import com.zzang.chongdae.domain.repository.ParticipantRepository
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

class ParticipantRepositoryImpl(
    private val participantRemoteDataSource: ParticipantRemoteDataSource,
) : ParticipantRepository {
    override suspend fun fetchParticipants(offeringId: Long): Result<Participants, DataError.Network> {
        return participantRemoteDataSource.fetchParticipants(
            offeringId,
        ).map { response ->
            response.toDomain()
        }
    }

    override suspend fun deleteParticipations(offeringId: Long): Result<Unit, DataError.Network> {
        return participantRemoteDataSource.deleteParticipations(offeringId).map { Unit }
    }
}
