package com.zzang.chongdae.data.remote.mapper

import com.zzang.chongdae.data.remote.dto.response.offering.MeetingsResponse
import com.zzang.chongdae.domain.model.Meetings

fun MeetingsResponse.toDomain() =
    Meetings(
        meetingDate = this.meetingDate.toLocalDateTime(),
        meetingAddress = this.meetingAddress,
        meetingAddressDetail = this.meetingAddressDetail,
        meetingAddressDong = this.meetingAddressDong,
    )
