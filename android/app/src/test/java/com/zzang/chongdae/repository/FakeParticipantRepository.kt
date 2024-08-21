package com.zzang.chongdae.repository

import com.zzang.chongdae.domain.model.participant.Participants
import com.zzang.chongdae.domain.repository.ParticipantRepository
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result
import com.zzang.chongdae.util.TestFixture.participants

class FakeParticipantRepository : ParticipantRepository {
    override suspend fun fetchParticipants(offeringId: Long): Result<Participants, DataError.Network> {
        return Result.Success(participants)
    }

    override suspend fun deleteParticipations(offeringId: Long): Result<Unit, DataError.Network> {
        return Result.Success(Unit)
    }
}
