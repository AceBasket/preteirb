package com.example.preteirb.data

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun storeUserId(userId: Int)
    suspend fun storeIsLoggedIn(isLoggedIn: Boolean)
    suspend fun saveToken(token: String)
    fun getUserId(): Flow<Int>
    fun getIsLoggedIn(): Flow<Boolean>
    fun getToken(): Flow<String?>
    suspend fun deleteToken()
}