package com.example.preteirb.data

import androidx.room.Embedded
import androidx.room.Relation


data class ItemsOwned(
    @Embedded
    val owner: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userOwnerId"
    )
    val items: List<Item>
)