package com.example.preteirb.api

import com.example.preteirb.data.user.User
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileApiService {
    @GET("profiles")
    suspend fun getProfiles(): List<User>

    @GET("profiles/{id}")
    suspend fun getProfile(id: Int): User

    @POST("profiles")
    suspend fun createProfile(user: User): User
}