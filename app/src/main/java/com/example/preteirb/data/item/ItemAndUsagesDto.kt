package com.example.preteirb.data.item

import com.example.preteirb.data.usage.UsageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemAndUsagesDto(
    val id: Int,
    val name: String,
    val description: String,
    val image: String?,
    @SerialName(value = "owner") val ownerId: Int,
    val usages: List<UsageDto>
)