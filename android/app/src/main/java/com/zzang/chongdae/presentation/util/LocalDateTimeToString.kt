package com.zzang.chongdae.presentation.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.toStringFormat(): String {
    return this.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"))
}

fun LocalTime.toStringFormat(): String {
    return this.format(DateTimeFormatter.ofPattern(("a h:mm"), Locale.KOREAN))
}
