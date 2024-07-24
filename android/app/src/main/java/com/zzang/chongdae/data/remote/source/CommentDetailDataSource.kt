package com.zzang.chongdae.data.remote.source

import com.zzang.chongdae.data.remote.dto.response.MeetingsResponse

interface CommentDetailDataSource {
    suspend fun getMeetings(offeringId: Long): Result<MeetingsResponse>
}
