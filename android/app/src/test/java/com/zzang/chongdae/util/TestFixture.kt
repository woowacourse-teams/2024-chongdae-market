package com.zzang.chongdae.util

import com.zzang.chongdae.data.mapper.toLocalDate
import com.zzang.chongdae.data.mapper.toLocalDateTime
import com.zzang.chongdae.data.mapper.toLocalTime
import com.zzang.chongdae.domain.model.Comment
import com.zzang.chongdae.domain.model.CommentCreatedAt
import com.zzang.chongdae.domain.model.Filter
import com.zzang.chongdae.domain.model.FilterName
import com.zzang.chongdae.domain.model.FilterType
import com.zzang.chongdae.domain.model.Meetings
import com.zzang.chongdae.domain.model.Offering
import com.zzang.chongdae.domain.model.OfferingCondition
import com.zzang.chongdae.domain.model.OfferingStatus
import com.zzang.chongdae.domain.model.ProductUrl
import okhttp3.MultipartBody

object TestFixture {
    val meetings: Meetings =
        Meetings(
            deadline = "2021-10-10T10:10:10".toLocalDateTime(),
            meetingAddress = "address",
            meetingAddressDetail = "addressDetail",
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

    val offeringStatus: OfferingStatus =
        OfferingStatus(
            imageUrl = "imageUrl",
            status = "status",
            buttonText = "buttonText",
        )

    val offeringStatus2: OfferingStatus =
        OfferingStatus(
            imageUrl = "imageUrl2",
            status = "status2",
            buttonText = "buttonText2",
        )

    val OFFERINGS_STUB =
        (0..20).map {
            Offering(
                id = it.toLong(),
                title = "",
                meetingAddress = "",
                thumbnailUrl = null,
                totalCount = 0,
                currentCount = 0,
                dividedPrice = 0,
                eachPrice = null,
                condition = OfferingCondition.CONFIRMED,
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

    val productUrl: ProductUrl = ProductUrl("url")

    val martiPartBody = MultipartBody.Part.createFormData("image", "image")
}
