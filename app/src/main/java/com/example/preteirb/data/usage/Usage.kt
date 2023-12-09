package com.example.preteirb.data.usage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

//@Entity(
//    tableName = "usages",
//    foreignKeys = [
//        ForeignKey(entity = Item::class, parentColumns = ["itemId"], childColumns = ["itemUsedId"]),
//        ForeignKey(
//            entity = User::class,
//            parentColumns = ["userId"],
//            childColumns = ["userUsingItemId"]
//        )
//    ],
//    indices = [
//        Index(value = ["userUsingItemId"]), Index(value = ["itemUsedId"])
//    ]
//)
//data class Usage(
//    @PrimaryKey(autoGenerate = true)
//    val usageId: Int = 1,
//    val itemUsedId: Int,
//    val userUsingItemId: Int,
//    val startDateTime: Long,
//    val endDateTime: Long
//)

@Serializable
data class Usage(
    val id: Int,
    @SerialName(value = "item") val itemId: Int,
    @SerialName(value = "user") val userId: Int,
    @SerialName(value = "start") val startDate: Long,
    @SerialName(value = "end") val endDate: Long
)

fun getShortenedDateFormat(dateTime: Long): String {
    return DateTimeFormatter.ofPattern("d MMM uuuu").withZone(ZoneId.systemDefault())
        .format(
            Instant.ofEpochMilli(
                dateTime
            )
        )
}

fun getLongDateFormat(dateTime: Long): String {
    return DateTimeFormatter.ofPattern("d MMMM uuuu").withZone(ZoneId.systemDefault())
        .format(
            Instant.ofEpochMilli(
                dateTime
            )
        )
}