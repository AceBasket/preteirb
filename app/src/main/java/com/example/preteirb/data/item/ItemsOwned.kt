package com.example.preteirb.data.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//data class ItemsOwned(
//    @Embedded
//    val owner: User,
//    @Relation(
//        parentColumn = "userId",
//        entityColumn = "userOwnerId"
//    )
//    val items: List<Item>
//)

@Serializable
data class ItemsOwned(
    val id: Int,
    val username: String,
    @SerialName(value = "items_owned") val items: List<Item>
)