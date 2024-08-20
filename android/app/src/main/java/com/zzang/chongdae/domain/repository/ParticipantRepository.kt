package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.participant.Participants

interface ParticipantRepository {
    suspend fun fetchParticipants(offeringId: Long): Result<Participants>
}
