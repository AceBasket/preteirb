package com.example.preteirb.data.usage

import androidx.room.Embedded
import com.example.preteirb.data.item.ItemWithOwner

data class UsageWithItemAndUser(
    @Embedded
    val item: ItemWithOwner,
    @Embedded
    val usage: Usage
)
