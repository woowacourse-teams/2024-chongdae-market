package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.domain.model.participant.Participants

interface ParticipantRepository {
    suspend fun fetchParticipants(offeringId: Long): Result<Participants, DataError.Network>

    suspend fun deleteParticipations(offeringId: Long): Result<Unit, DataError.Network>

    suspend fun patchNickname(nickname: String): Result<Unit, DataError.Network>
}
