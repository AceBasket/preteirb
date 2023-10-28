package com.example.preteirb.data.item

import androidx.room.Embedded
import androidx.room.Relation
import com.example.preteirb.data.usage.Usage

data class ItemAndUsages(
    @Embedded
    val item: Item,
    @Relation(
        parentColumn = "itemId",
        entityColumn = "itemId"
    )
    val usages: List<Usage>
)