package com.zzang.chongdae.data.source

import com.zzang.chongdae.common.handler.DataError
import com.zzang.chongdae.common.handler.Result
import com.zzang.chongdae.data.remote.dto.request.NicknameRequest
import com.zzang.chongdae.data.remote.dto.response.participants.ParticipantsResponse

interface ParticipantRemoteDataSource {
    suspend fun fetchParticipants(offeringId: Long): Result<ParticipantsResponse, DataError.Network>

    suspend fun deleteParticipations(offeringId: Long): Result<Unit, DataError.Network>

    suspend fun patchNickname(nicknameRequest: NicknameRequest): Result<Unit, DataError.Network>
}
