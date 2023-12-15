package com.example.preteirb.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Entity(
//    tableName = "users",
//)
//data class User(
//    @PrimaryKey(autoGenerate = true)
//    val userId: Int = 1,
//    val username: String,
//)

@Serializable
data class User(
    val id: Int,
    val username: String,
    @SerialName(value = "profile_pic") val profilePicture: String?,
)