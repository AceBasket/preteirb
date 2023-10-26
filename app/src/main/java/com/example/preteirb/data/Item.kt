package com.example.preteirb.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "items"
)
data class Item(
    @PrimaryKey(autoGenerate = true)
    val itemId: Int = 1,
    val name: String,
    val description: String,
    val userOwnerId: Int,
)