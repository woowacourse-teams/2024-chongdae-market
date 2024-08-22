package com.zzang.chongdae.presentation.view.commentdetail.model.meeting

import com.zzang.chongdae.domain.model.Meetings
import java.time.LocalDateTime

class MeetingsUiModel(
    val meetingDate: LocalDateTime,
    val meetingAddress: String,
    val meetingAddressDetail: String?,
) {
    companion object {
        fun Meetings.toUiModel(): MeetingsUiModel {
            return MeetingsUiModel(
                meetingDate = meetingDate,
                meetingAddress = meetingAddress,
                meetingAddressDetail = meetingAddressDetail,
            )
        }
    }
}
