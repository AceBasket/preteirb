package com.example.preteirb.data.usage

import com.example.preteirb.data.item.ItemWithOwner
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//data class UsageWithItemAndUser(
//    @Embedded
//    val item: ItemWithOwner,
//    @Embedded
//    val usage: Usage
//)

@Serializable
data class UsageWithItemAndUser(
    val id: Int,
    @SerialName(value = "start") val startDate: Long,
    @SerialName(value = "end") val endDate: Long,
    val item: ItemWithOwner
)