package com.example.preteirb.data.usage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Serializable
data class UsageDto(
    val id: Int,
    @SerialName(value = "item") val itemUsedId: Int,
    @SerialName(value = "user") val userUsingItemId: Int,
    @SerialName(value = "start") val startDate: String,
    @SerialName(value = "end") val endDate: String
)

data class Usage(
    val id: Int,
    val itemUsedId: Int,
    val userUsingItemId: Int,
    val startDateTime: Long,
    val endDateTime: Long
)

fun UsageDto.toUsage() = Usage(
    id = id,
    itemUsedId = itemUsedId,
    userUsingItemId = userUsingItemId,
    startDateTime = getDateTime(startDate),
    endDateTime = getDateTime(endDate)
)

fun Usage.toUsageWithStringDate() = UsageDto(
    id = id,
    itemUsedId = itemUsedId,
    userUsingItemId = userUsingItemId,
    startDate = getDashedNumeralDateFormat(startDateTime),
    endDate = getDashedNumeralDateFormat(endDateTime)
)

fun getDateTime(date: String): Long {
//    convert date which is in format "u-M-d" to epoch milliseconds
    return LocalDate.parse(date).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun getDashedNumeralDateFormat(dateTime: Long): String {
    return DateTimeFormatter.ofPattern("u-M-d").withZone(ZoneId.systemDefault())
        .format(
            Instant.ofEpochMilli(
                dateTime
            )
        )
}

fun getShortenedDateFormat(dateTime: Long): String {
    return DateTimeFormatter.ofPattern("d MMM uuuu").withZone(ZoneId.systemDefault())
        .format(
            Instant.ofEpochMilli(
                dateTime
            )
        )
}