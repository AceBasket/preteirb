package com.example.preteirb.data.usage

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.preteirb.data.item.Item
import com.example.preteirb.data.user.User

@Entity(
    tableName = "usages",
    foreignKeys = [
        ForeignKey(entity = Item::class, parentColumns = ["itemId"], childColumns = ["itemId"]),
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["userId"])
    ],
    indices = [
        Index(value = ["userId"]), Index(value = ["itemId"])
    ]
)
data class Usage(
    @PrimaryKey(autoGenerate = true)
    val usageId: Int = 1,
    val itemId: Int,
    val userId: Int,
    val startDateTime: Long,
    val endDateTime: Long
)