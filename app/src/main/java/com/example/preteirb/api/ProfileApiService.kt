package com.example.preteirb.api

import com.example.preteirb.data.item.ItemDto
import com.example.preteirb.data.usage.UsageWithItemAndUserDto
import com.example.preteirb.data.user.UserDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileApiService {
    @GET("profiles/")
    suspend fun getProfiles(): List<UserDto>

    @GET("profiles/{id}/")
    suspend fun getProfile(@Path("id") id: Int): UserDto

    @Multipart
    @POST("profiles/")
    suspend fun createProfile(
        @Part profilePicture: MultipartBody.Part,
        @Part("username") username: RequestBody,
    ): UserDto

    @POST("profiles/")
    suspend fun createProfileWithoutPicture(@Body userDto: UserDto): UserDto

    @DELETE("profiles/{id}/")
    suspend fun deleteProfile(@Path("id") id: Int)

    @Multipart
    @PUT("profiles/{id}/")
    suspend fun updateProfile(
        @Path("id") id: Int, @Part profilePicture: MultipartBody.Part,
        @Part("username") username: RequestBody,
    ): UserDto

    @PUT("profiles/{id}/")
    suspend fun updateProfileWithoutPicture(@Path("id") id: Int, @Body userDto: UserDto): UserDto

    @Multipart
    @PATCH("profiles/{id}/")
    suspend fun updateUsername(
        @Path("id") id: Int,
        @Part("username") username: RequestBody,
    ): UserDto

    @GET("profiles/{id}/items_owned/")
    suspend fun getItemsOwnedByUser(@Path("id") id: Int): List<ItemDto>

    @GET("profiles/{id}/usages/")
    suspend fun getUsagesAndItemWithOwnerByUser(@Path("id") id: Int): List<UsageWithItemAndUserDto>
}