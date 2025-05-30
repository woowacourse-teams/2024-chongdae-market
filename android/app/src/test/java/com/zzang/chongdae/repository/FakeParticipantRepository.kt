package com.zzang.chongdae.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.participant.Participants
import com.zzang.chongdae.domain.repository.ParticipantRepository
import com.zzang.chongdae.util.TestFixture.participants

class FakeParticipantRepository : ParticipantRepository {
    override suspend fun fetchParticipants(offeringId: Long): Result<Participants, DataError.Network> {
        return Result.Success(participants)
    }

    override suspend fun deleteParticipations(offeringId: Long): Result<Unit, DataError.Network> {
        return Result.Success(Unit)
    }

    override suspend fun patchNickname(nickname: String): Result<Unit, DataError.Network> {
        TODO("Not yet implemented")
    }
}
