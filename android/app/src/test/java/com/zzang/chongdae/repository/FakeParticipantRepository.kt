package com.zzang.chongdae.repository

import com.zzang.chongdae.domain.model.participant.Participants
import com.zzang.chongdae.domain.repository.ParticipantRepository
import com.zzang.chongdae.util.TestFixture.participants

class FakeParticipantRepository : ParticipantRepository {
    override suspend fun fetchParticipants(offeringId: Long): Result<Participants> {
        return Result.success(participants)
    }
}
