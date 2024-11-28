package com.zzang.chongdae.data.repository

import com.zzang.chongdae.data.mapper.participant.toDomain
import com.zzang.chongdae.data.mapper.toDomain
import com.zzang.chongdae.data.source.ParticipantRemoteDataSource
import com.zzang.chongdae.domain.model.participant.Participants
import com.zzang.chongdae.domain.repository.ParticipantRepository

class ParticipantRepositoryImpl(
    private val participantRemoteDataSource: ParticipantRemoteDataSource,
) : ParticipantRepository {
    override suspend fun fetchParticipants(offeringId: Long): Result<Participants> {
        return participantRemoteDataSource.fetchParticipants(
            offeringId,
        ).mapCatching { response ->
            response.toDomain()
        }
    }
}
