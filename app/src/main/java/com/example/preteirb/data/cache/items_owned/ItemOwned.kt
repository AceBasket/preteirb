package com.example.preteirb.data.cache.items_owned

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.preteirb.data.item.Item
import com.example.preteirb.model.items_owned.ItemDetails

@Entity(
    tableName = "items_owned"
)
data class ItemOwned(
    @PrimaryKey
    val id: Int,
    var name: String,
    val description: String,
    val imageUrl: String?,
    val ownerId: Int,
)

fun Item.toItemOwned(): ItemOwned {
    return ItemOwned(
        id = id,
        name = name,
        description = description,
        imageUrl = image,
        ownerId = ownerId,
    )
}

fun ItemOwned.toItem(): Item {
    return Item(
        id = id,
        name = name,
        description = description,
        image = imageUrl,
        ownerId = ownerId,
    )
}

fun ItemOwned.toItemDetails(): ItemDetails {
    return ItemDetails(
        id = id,
        name = name,
        description = description,
        image = Uri.parse(imageUrl ?: ""),
        ownerId = ownerId,
    )
}