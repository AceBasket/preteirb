package com.example.preteirb.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val username: String,
    @SerialName(value = "profile_pic") val profilePicture: String?,
)