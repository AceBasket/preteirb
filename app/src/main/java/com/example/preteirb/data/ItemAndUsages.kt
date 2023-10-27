package com.example.preteirb.data

import androidx.room.Relation

data class ItemAndUsages(
    val item: Item,
    @Relation(
        parentColumn = "itemId",
        entityColumn = "itemId"
    )
    val usages: List<Usage>
)
