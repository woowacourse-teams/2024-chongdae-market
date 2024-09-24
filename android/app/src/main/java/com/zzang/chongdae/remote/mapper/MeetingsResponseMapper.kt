package com.zzang.chongdae.remote.mapper

import com.zzang.chongdae.domain.model.Meetings
import com.zzang.chongdae.remote.dto.response.offering.MeetingsResponse

fun MeetingsResponse.toDomain() =
    Meetings(
        meetingDate = this.meetingDate.toLocalDateTime(),
        meetingAddress = this.meetingAddress,
        meetingAddressDetail = this.meetingAddressDetail,
        meetingAddressDong = this.meetingAddressDong,
    )
