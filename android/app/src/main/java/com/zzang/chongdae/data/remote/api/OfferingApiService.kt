package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.data.remote.dto.request.ProductUrlRequest
import com.zzang.chongdae.data.remote.dto.response.MeetingsResponse
import com.zzang.chongdae.data.remote.dto.response.OfferingDetailResponse
import com.zzang.chongdae.data.remote.dto.response.OfferingsResponse
import com.zzang.chongdae.data.remote.dto.response.ProductUrlResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OfferingApiService {
    @GET("/offerings")
    suspend fun getOfferings(
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
    
    @POST("/offerings/product-images/og")
    suspend fun postProductImageOg(
        @Body productUrl: ProductUrlRequest,
    ): Response<ProductUrlResponse>
}
