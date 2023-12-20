package com.example.preteirb.data.item

import com.example.preteirb.data.user.UserDto
import kotlinx.serialization.Serializable

@Serializable
data class ItemWithOwner(
    val id: Int,
    val name: String,
    val description: String,
    val image: String?,
    val owner: UserDto
)