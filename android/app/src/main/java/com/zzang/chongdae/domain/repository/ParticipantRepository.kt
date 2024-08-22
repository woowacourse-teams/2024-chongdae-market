package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.participant.Participants
import com.zzang.chongdae.domain.util.DataError
import com.zzang.chongdae.domain.util.Result

interface ParticipantRepository {
    suspend fun fetchParticipants(offeringId: Long): Result<Participants, DataError.Network>

    suspend fun deleteParticipations(offeringId: Long): Result<Unit, DataError.Network>
}
