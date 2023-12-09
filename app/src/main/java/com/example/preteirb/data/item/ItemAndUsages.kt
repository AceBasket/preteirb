package com.example.preteirb.data.item

import com.example.preteirb.data.usage.Usage
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
    val usages: List<Usage>
)