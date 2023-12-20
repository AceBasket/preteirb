package com.example.preteirb.data.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Int,
    val name: String,
    val description: String,
    val image: String?,
    @SerialName(value = "owner") val ownerId: Int,
)