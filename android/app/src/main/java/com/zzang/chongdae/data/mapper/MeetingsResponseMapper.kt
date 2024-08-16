package com.zzang.chongdae.data.mapper

import com.zzang.chongdae.data.remote.dto.response.MeetingsResponse
import com.zzang.chongdae.domain.model.Meetings

fun MeetingsResponse.toDomain() =
    Meetings(
        meetingDate = this.meetingDate.toLocalDateTime(),
        meetingAddress = this.meetingAddress,
        meetingAddressDetail = this.meetingAddressDetail,
        meetingAddressDong = this.meetingAddressDong,
    )
