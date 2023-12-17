package com.example.preteirb.data.item

import com.example.preteirb.data.user.User
import kotlinx.serialization.Serializable

//data class ItemWithOwner(
//    @Embedded
//    val item: Item,
//    @Embedded
//    val owner: User
//)

@Serializable
data class ItemWithOwner(
    val id: Int,
    val name: String,
    val description: String,
    val image: String?,
    val owner: User
)