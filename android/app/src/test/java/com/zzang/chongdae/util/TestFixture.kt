package com.zzang.chongdae.util

import com.zzang.chongdae.data.mapper.toLocalDate
import com.zzang.chongdae.data.mapper.toLocalDateTime
import com.zzang.chongdae.data.mapper.toLocalTime
import com.zzang.chongdae.domain.model.Comment
import com.zzang.chongdae.domain.model.CommentCreatedAt
import com.zzang.chongdae.domain.model.Meetings
import com.zzang.chongdae.domain.model.OfferingStatus

object TestFixture {
    val meetings: Meetings = Meetings(
        deadline = "2021-10-10T10:10:10".toLocalDateTime(),
        meetingAddress = "address",
        meetingAddressDetail = "addressDetail"
    )
    
    val comment1: Comment = Comment(
        content = "content1",
        commentCreatedAt = CommentCreatedAt(
            "2021-10-10".toLocalDate(),
            "10:10:10".toLocalTime(),
        ),
        isMine = true,
        isProposer = true,
        nickname = "nickname"
    )
    
    val comment2: Comment = Comment(
        content = "content2",
        commentCreatedAt = CommentCreatedAt(
            "2021-10-10".toLocalDate(),
            "10:10:10".toLocalTime(),
        ),
        isMine = true,
        isProposer = true,
        nickname = "nickname"
    )
    
    val comments1: MutableList<Comment> = mutableListOf(
        comment1
    )
    
    val comments2: List<Comment> = listOf(
        comment2
    )
    
    val offeringStatus: OfferingStatus = OfferingStatus(
        imageUrl = "imageUrl",
        status = "status",
        buttonText = "buttonText"
    )
    
    val offeringStatus2: OfferingStatus = OfferingStatus(
        imageUrl = "imageUrl2",
        status = "status2",
        buttonText = "buttonText2"
    )
}
