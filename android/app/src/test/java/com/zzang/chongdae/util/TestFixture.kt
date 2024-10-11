package com.zzang.chongdae.util

import com.zzang.chongdae.data.remote.mapper.toLocalDate
import com.zzang.chongdae.data.remote.mapper.toLocalDateTime
import com.zzang.chongdae.data.remote.mapper.toLocalTime
import com.zzang.chongdae.domain.model.Comment
import com.zzang.chongdae.domain.model.CommentCreatedAt
import com.zzang.chongdae.domain.model.CommentOfferingInfo
import com.zzang.chongdae.domain.model.CommentRoom
import com.zzang.chongdae.domain.model.CurrentCount
import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.FilterName
import com.zzang.chongdae.domain.model.FilterType
import com.zzang.chongdae.domain.model.Meetings
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.model.OfferingCondition
import com.zzang.chongdae.domain.model.OfferingDetail
import com.zzang.chongdae.domain.model.ProductUrl
import com.zzang.chongdae.domain.model.participant.Participant
import com.zzang.chongdae.domain.model.participant.ParticipantCount
import com.zzang.chongdae.domain.model.participant.Participants
import com.zzang.chongdae.domain.model.participant.Proposer
import okhttp3.MultipartBody
import java.time.LocalDateTime

object TestFixture {
    val meetings: Meetings =
        Meetings(
            meetingDate = "2021-10-10T10:10:10".toLocalDateTime(),
            meetingAddress = "address",
            meetingAddressDetail = "addressDetail",
            meetingAddressDong = null,
        )

    val comment: Comment =
        Comment(
            content = "content1",
            commentCreatedAt =
                CommentCreatedAt(
                    "2021-10-10".toLocalDate(),
                    "10:10:10".toLocalTime(),
                ),
            isMine = true,
            isProposer = true,
            nickname = "nickname",
        )

    val comments: MutableList<Comment> =
        mutableListOf(
            comment,
        )

    val commentOfferingInfo: CommentOfferingInfo =
        CommentOfferingInfo(
            status = "status",
            imageUrl = "imageUrl",
            buttonText = "buttonText",
            message = "message",
            title = "title",
            isProposer = true,
        )

    val commentOfferingInfo2: CommentOfferingInfo =
        CommentOfferingInfo(
            imageUrl = "imageUrl2",
            status = "status2",
            buttonText = "buttonText2",
            message = "message2",
            title = "title2",
            isProposer = false,
        )

    val participants: Participants =
        Participants(
            proposer = Proposer(nickname = "proposer nickname"),
            participants =
                listOf(
                    Participant(nickname = "participant nickname"),
                ),
            participantCount = ParticipantCount(currentCount = 1, totalCount = 4),
            price = 1000,
        )

    val OFFERINGS_STUB =
        (0..20).map {
            Offering(
                id = it.toLong(),
                title = "",
                meetingAddressDong = "",
                thumbnailUrl = null,
                totalCount = 0,
                currentCount = 0,
                dividedPrice = 0,
                originPrice = null,
                status = OfferingCondition.CONFIRMED,
                isOpen = false,
            )
        }
    val FILTERS_STUB =
        (0..3).map {
            Filter(
                name = FilterName.HIGH_DISCOUNT,
                value = "",
                type = FilterType.VISIBLE,
            )
        }

    val COMMENT_ROOMS_STUB =
        listOf(
            CommentRoom(
                id = 1,
                title = "title",
                latestComment = "latestComment",
                latestCommentTime = LocalDateTime.of(1, 1, 1, 0, 0),
                isProposer = true,
            ),
        )

    val productUrl: ProductUrl = ProductUrl("url")

    val martiPartBody = MultipartBody.Part.createFormData("image", "image")

    val OFFERING_DETAIL_STUB =
        OfferingDetail(
            id = 1,
            title = "Test",
            nickname = "Test",
            isProposer = true,
            productUrl = "TEST",
            thumbnailUrl = null,
            dividedPrice = 1000,
            totalPrice = 1000,
            meetingDate = LocalDateTime.of(1, 1, 1, 1, 1, 1, 1),
            currentCount = CurrentCount(value = 1000),
            totalCount = 1000,
            meetingAddress = "TEST",
            meetingAddressDetail = "TEST",
            description = "TEST",
            condition = OfferingCondition.CONFIRMED,
            isParticipated = false,
            originPrice = 1000,
        )
}
