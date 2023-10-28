package com.example.preteirb.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 1,
    val username: String,
    val location: String,
)
