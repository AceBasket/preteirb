package com.example.preteirb.data.item

import androidx.room.Embedded
import com.example.preteirb.data.user.User

data class ItemWithOwner(
    @Embedded
    val item: Item,
    @Embedded
    val owner: User
)
