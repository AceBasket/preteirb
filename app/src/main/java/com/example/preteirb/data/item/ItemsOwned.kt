package com.example.preteirb.data.item

import androidx.room.Embedded
import androidx.room.Relation
import com.example.preteirb.data.user.User

data class ItemsOwned(
    @Embedded
    val owner: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userOwnerId"
    )
    val items: List<Item>
)