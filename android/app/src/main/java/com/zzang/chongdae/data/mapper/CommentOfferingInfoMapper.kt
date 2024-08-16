package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.response.comment.CommentOfferingInfoResponse
import com.zzang.chongdae.domain.model.CommentOfferingInfo

fun CommentOfferingInfoResponse.toDomain() =
    CommentOfferingInfo(
        status = this.status,
        imageUrl = this.imageUrl,
        buttonText = this.buttonText,
        message = this.message,
        title = this.title,
        isProposer = this.isProposer,
    )
