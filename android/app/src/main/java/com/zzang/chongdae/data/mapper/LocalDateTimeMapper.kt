package com.zzang.chongdae.data.mapper

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun String.toLocalDateTime(): LocalDateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

fun String.toLocalDate(): LocalDate = LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)

fun String.toLocalTime(): LocalTime = LocalTime.parse(this, DateTimeFormatter.ISO_LOCAL_TIME)
