package com.example.preteirb.data.usage

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.preteirb.data.item.Item
import com.example.preteirb.data.user.User
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Entity(
    tableName = "usages",
    foreignKeys = [
        ForeignKey(entity = Item::class, parentColumns = ["itemId"], childColumns = ["itemUsedId"]),
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userUsingItemId"]
        )
    ],
    indices = [
        Index(value = ["userUsingItemId"]), Index(value = ["itemUsedId"])
    ]
)
data class Usage(
    @PrimaryKey(autoGenerate = true)
    val usageId: Int = 1,
    val itemUsedId: Int,
    val userUsingItemId: Int,
    val startDateTime: Long,
    val endDateTime: Long
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