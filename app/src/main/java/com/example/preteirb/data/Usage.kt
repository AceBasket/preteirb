package com.example.preteirb.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "usages",
    primaryKeys = ["itemId", "userId"],
    foreignKeys = [
        ForeignKey(entity = Item::class, parentColumns = ["itemId"], childColumns = ["itemId"]),
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["userId"])
    ],
    indices = [
        Index(value = ["userId"])
    ]
)
data class Usage(
    val itemId: Int,
    val userId: Int,
    val startDateTime: String,
    val endDateTime: String
)
