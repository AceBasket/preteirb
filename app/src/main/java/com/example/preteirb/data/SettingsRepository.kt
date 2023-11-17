package com.example.preteirb.data

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun storeUserId(userId: Int)
    suspend fun storeIsLoggedIn(isLoggedIn: Boolean)
    fun getUserId(): Flow<Int>
    fun getIsLoggedIn(): Flow<Boolean>
}