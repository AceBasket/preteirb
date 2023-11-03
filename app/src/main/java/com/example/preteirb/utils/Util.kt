package com.example.preteirb.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

fun getLocalDateTimeFromEpochMilli(epochMilli: Long): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneOffset.UTC)
}