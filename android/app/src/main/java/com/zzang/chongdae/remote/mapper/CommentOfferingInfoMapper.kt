package com.zzang.chongdae.remote.mapper

import com.zzang.chongdae.domain.model.CommentOfferingInfo
import com.zzang.chongdae.remote.dto.response.comment.CommentOfferingInfoResponse

fun CommentOfferingInfoResponse.toDomain() =
    CommentOfferingInfo(
        status = this.status,
        imageUrl = this.imageUrl,
        buttonText = this.buttonText,
        message = this.message,
        title = this.title,
        isProposer = this.isProposer,
    )
