package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.data.remote.dto.response.MeetingsResponse
import com.zzang.chongdae.data.remote.dto.response.OfferingDetailResponse
import com.zzang.chongdae.data.remote.dto.response.OfferingsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OfferingsApiService {
    @GET("/offerings")
    suspend fun getArticles(
        @Query("last-id") lastOfferingId: Long,
        @Query("page-size") pageSize: Int,
    ): Response<OfferingsResponse>

    @GET("/offerings/{offering-id}")
    suspend fun getOfferingDetail(
        @Path("offering-id") offeringId: Long,
        @Query("member-id") memberId: Long,
    ): Response<OfferingDetailResponse>

    @GET("/offerings/{offering-id}/meetings")
    suspend fun getMeetings(
        @Path("offering-id") offeringId: Long,
    ): Response<MeetingsResponse>

    @POST("/offerings")
    suspend fun postOfferingWrite(
        @Body offeringWriteRequest: OfferingWriteRequest,
    ): Response<Unit>
}
