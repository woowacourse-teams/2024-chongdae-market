package com.zzang.chongdae.domain.repository

import com.zzang.chongdae.domain.model.Meetings

interface CommentDetailRepository {
    suspend fun getMeetings(offeringId: Long): Result<Meetings>
}
