package com.example.preteirb.data.usage

import com.example.preteirb.data.item.ItemWithOwner
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsageWithItemAndUserStringDate(
    val id: Int,
    @SerialName(value = "start") val startDate: String,
    @SerialName(value = "end") val endDate: String,
    val item: ItemWithOwner,
    val user: Int
)

data class UsageWithItemAndUser(
    val id: Int,
    val startDate: Long,
    val endDate: Long,
    val item: ItemWithOwner,
    val user: Int
)

fun UsageWithItemAndUserStringDate.toUsageWithItemAndUser(): UsageWithItemAndUser {
    return UsageWithItemAndUser(
        id = id,
        startDate = getDateTime(startDate),
        endDate = getDateTime(endDate),
        item = item,
        user = user
    )
}
