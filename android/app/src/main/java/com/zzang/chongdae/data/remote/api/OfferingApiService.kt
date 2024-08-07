package com.zzang.chongdae.data.remote.api

import com.zzang.chongdae.data.remote.dto.request.OfferingWriteRequest
import com.zzang.chongdae.data.remote.dto.request.ProductUrlRequest
import com.zzang.chongdae.data.remote.dto.response.FiltersResponse
import com.zzang.chongdae.data.remote.dto.response.MeetingsResponse
import com.zzang.chongdae.data.remote.dto.response.OfferingDetailResponse
import com.zzang.chongdae.data.remote.dto.response.OfferingStatusResponse
import com.zzang.chongdae.data.remote.dto.response.OfferingsResponse
import com.zzang.chongdae.data.remote.dto.response.ProductUrlResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface OfferingApiService {
    @GET("/offerings")
    suspend fun getOfferings(
        @Query("filter") filter: String?,
        @Query("search") search: String?,
        @Query("last-id") lastOfferingId: Long?,
        @Query("page-size") pageSize: Int?,
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

    @GET("/offerings/filters")
    suspend fun getFilters(): Response<FiltersResponse>

    @POST("/offerings")
    suspend fun postOfferingWrite(
        @Body offeringWriteRequest: OfferingWriteRequest,
    ): Response<Unit>

    @POST("/offerings/product-images/og")
    suspend fun postProductImageOg(
        @Body productUrl: ProductUrlRequest,
    ): Response<ProductUrlResponse>

    @Multipart
    @POST("/offerings/product-images/s3")
    suspend fun postProductImageS3(
        @Part image: MultipartBody.Part,
    ): Response<ProductUrlResponse>

    @GET("/offerings/{offering-id}/status")
    suspend fun getOfferingStatus(
        @Path("offering-id") offeringId: Long,
    ): Response<OfferingStatusResponse>
    
    @PATCH("/offerings/{offering-id}/status")
    suspend fun patchOfferingStatus(
        @Path("offering-id") offeringId: Long,
    ): Response<Unit>
}
