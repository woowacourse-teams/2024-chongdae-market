package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.dto.request.ParticipationRequest
import com.zzang.chongdae.data.remote.dto.response.GroupPurchaseDetailResponse
import com.zzang.chongdae.data.remote.dto.response.GroupPurchasesResponse
import com.zzang.chongdae.data.remote.dto.response.ParticipationResponse

interface GroupPurchaseDataSource {
    suspend fun fetchGroupPurchases(): Result<GroupPurchasesResponse>

    suspend fun fetchGroupPurchaseDetail(id: Long): Result<GroupPurchaseDetailResponse>

    suspend fun participateGroupPurchase(participationRequest: ParticipationRequest): Result<ParticipationResponse>
}
