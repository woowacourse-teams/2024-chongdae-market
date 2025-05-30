package com.zzang.chongdae.data.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.dto.request.NicknameRequest
import com.zzang.chongdae.data.remote.mapper.participant.toDomain
import com.zzang.chongdae.data.source.ParticipantRemoteDataSource
import com.zzang.chongdae.di.annotations.ParticipantDataSourceQualifier
import com.zzang.chongdae.domain.model.participant.Participants
import com.zzang.chongdae.domain.repository.ParticipantRepository
import javax.inject.Inject

class ParticipantRepositoryImpl
    @Inject
    constructor(
        @ParticipantDataSourceQualifier private val participantRemoteDataSource: ParticipantRemoteDataSource,
    ) : ParticipantRepository {
        override suspend fun fetchParticipants(offeringId: Long): Result<Participants, DataError.Network> =
            participantRemoteDataSource.fetchParticipants(
                offeringId,
            ).map { response ->
                response.toDomain()
            }

        override suspend fun deleteParticipations(offeringId: Long): Result<Unit, DataError.Network> =
            participantRemoteDataSource.deleteParticipations(offeringId)

        override suspend fun patchNickname(nickname: String): Result<Unit, DataError.Network> {
            return participantRemoteDataSource.patchNickname(
                NicknameRequest(nickname),
            ).map { Unit }
        }
    }
