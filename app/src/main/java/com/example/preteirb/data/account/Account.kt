package com.example.preteirb.data.account

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
    val username: String,
    val password: String,
)

@Serializable
data class LoginResponseDto(
    val username: String,
    val token: String,
    val id: Int,
)