package com.zzang.chongdae.domain.model

import java.time.LocalDateTime

data class Meetings(
    val meetingDate: LocalDateTime,
    val meetingAddress: String,
    val meetingAddressDetail: String,
    val meetingAddressDong: String?,
)
