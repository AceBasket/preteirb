package com.example.preteirb.api

import com.example.preteirb.data.item.Item
import com.example.preteirb.data.usage.UsageWithItemAndUser
import com.example.preteirb.data.user.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ProfileApiService {
    @GET("profiles/")
    suspend fun getProfiles(): List<User>

    @GET("profiles/{id}")
    suspend fun getProfile(@Path("id") id: Int): User

    @POST("profiles/")
    suspend fun createProfile(@Body user: User): User

    @DELETE("profiles/{id}")
    suspend fun deleteProfile(@Path("id") id: Int)

    @PATCH("profiles/{id}")
    suspend fun updateProfile(@Path("id") id: Int, user: User)

    @GET("profiles/{id}/items_owned")
    suspend fun getItemsOwnedByUser(@Path("id") id: Int): List<Item>

    @GET("profiles/{id}/usages")
    suspend fun getUsagesAndItemWithOwnerByUser(@Path("id") id: Int): List<UsageWithItemAndUser>
}