package com.example.preteirb.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

fun getLocalDateTimeFromEpoch(epoch: Long): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneOffset.UTC)
}