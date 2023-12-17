package com.example.preteirb.data.item

import com.example.preteirb.data.usage.UsageWithStringDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//data class ItemAndUsages(
//    @Embedded
//    val item: Item,
//    @Relation(
//        parentColumn = "itemId",
//        entityColumn = "itemUsedId"
//    )
//    val usages: List<Usage>
//)

@Serializable
data class ItemAndUsages(
    val id: Int,
    val name: String,
    val description: String,
    val image: String?,
    @SerialName(value = "owner") val ownerId: Int,
    val usages: List<UsageWithStringDate>
)