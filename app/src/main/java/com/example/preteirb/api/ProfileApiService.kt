package com.example.preteirb.api

import com.example.preteirb.data.item.Item
import com.example.preteirb.data.usage.UsageWithItemAndUserStringDate
import com.example.preteirb.data.user.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileApiService {
    @GET("profiles/")
    suspend fun getProfiles(): List<User>

    @GET("profiles/{id}/")
    suspend fun getProfile(@Path("id") id: Int): User

    @Multipart
    @POST("profiles/")
    suspend fun createProfile(
        @Part profilePicture: MultipartBody.Part,
        @Part("username") username: RequestBody,
    ): User

    @POST("profiles/")
    suspend fun createProfileWithoutPicture(@Body user: User): User

    @DELETE("profiles/{id}/")
    suspend fun deleteProfile(@Path("id") id: Int)

    @Multipart
    @PUT("profiles/{id}/")
    suspend fun updateProfile(
        @Path("id") id: Int, @Part profilePicture: MultipartBody.Part,
        @Part("username") username: RequestBody,
    ): User

    @PUT("profiles/{id}/")
    suspend fun updateProfileWithoutPicture(@Body user: User): User

    @GET("profiles/{id}/items_owned/")
    suspend fun getItemsOwnedByUser(@Path("id") id: Int): List<Item>

    @GET("profiles/{id}/usages/")
    suspend fun getUsagesAndItemWithOwnerByUser(@Path("id") id: Int): List<UsageWithItemAndUserStringDate>
}