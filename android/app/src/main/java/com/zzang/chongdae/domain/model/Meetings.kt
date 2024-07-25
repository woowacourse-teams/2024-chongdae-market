package com.zzang.chongdae.domain.model

import java.time.LocalDateTime

data class Meetings(
    val deadline: LocalDateTime,
    val meetingAddress: String,
    val meetingAddressDetail: String,
)
