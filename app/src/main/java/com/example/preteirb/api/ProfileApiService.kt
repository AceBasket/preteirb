package com.example.preteirb.api

import com.example.preteirb.data.item.ItemsOwned
import com.example.preteirb.data.usage.UsageWithItemAndUser
import com.example.preteirb.data.user.User
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ProfileApiService {
    @GET("profiles")
    suspend fun getProfiles(): List<User>

    @GET("profiles/{id}")
    suspend fun getProfile(id: Int): User

    @POST("profiles")
    suspend fun createProfile(user: User): Long

    @DELETE("profiles/{id}")
    suspend fun deleteProfile(id: Int)

    @PATCH("profiles/{id}")
    suspend fun updateProfile(id: Int, user: User)

    @GET("profiles/{id}/items_owned")
    suspend fun getItemsOwnedByUser(id: Int): ItemsOwned

    @GET("profiles/{id}/usages")
    suspend fun getUsagesAndItemWithOwnerByUser(id: Int): List<UsageWithItemAndUser>
}