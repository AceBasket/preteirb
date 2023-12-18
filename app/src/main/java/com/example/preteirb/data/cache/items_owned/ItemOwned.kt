package com.example.preteirb.data.cache.items_owned

import androidx.room.Entity
import com.example.preteirb.data.item.Item

@Entity(
    tableName = "items_owned"
)
data class ItemOwned(
    val id: Int,
    var name: String,
    val description: String,
    val imageUrl: String,
)

fun Item.toItemOwned(): ItemOwned {
    return ItemOwned(
        id = id,
        name = name,
        description = description,
        imageUrl = image ?: ""
    )
}