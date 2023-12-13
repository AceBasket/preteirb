package com.example.preteirb.data.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Entity(
//    tableName = "items"
//)
//data class Item(
//    @PrimaryKey(autoGenerate = true)
//    val itemId: Int = 1,
//    var name: String,
//    val description: String,
//    val userOwnerId: Int,
//)

@Serializable
data class Item(
    val id: Int,
    val name: String,
    val description: String,
    @SerialName(value = "owner") val ownerId: Int,
)