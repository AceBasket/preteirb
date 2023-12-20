package com.example.preteirb.api

import com.example.preteirb.data.item.ItemAndUsagesDto
import com.example.preteirb.data.item.ItemDto
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
import retrofit2.http.Query

interface ItemApiService {
    @GET("items/")
    suspend fun getItems(): List<ItemDto>

    @GET("items/")
    suspend fun searchItems(@Query("search") search: String): List<ItemDto>

    @GET("items/{id}/")
    suspend fun getItem(@Path("id") id: Int): ItemDto

    @DELETE("items/{id}/")
    suspend fun deleteItem(@Path("id") id: Int)

    @PUT("items/{id}/")
    suspend fun updateItemWithoutImage(@Path("id") id: Int, @Body itemDto: ItemDto): ItemDto

    @Multipart
    @PATCH("items/{id}/")
    suspend fun updateItemName(@Path("id") id: Int, @Part("name") name: RequestBody): ItemDto

    @Multipart
    @PUT("items/{id}/")
    suspend fun updateItem(
        @Path("id") id: Int,
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("owner") owner: RequestBody
    ): ItemDto

    @Multipart
    @POST("items/")
    suspend fun createItem(
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("owner") owner: RequestBody
    ): ItemDto

    @POST("items/")
    suspend fun createItemWithoutImage(@Body itemDto: ItemDto): ItemDto

    @GET("items/{id}/usages/")
    suspend fun getUsagesByItem(@Path("id") id: Int): ItemAndUsagesDto
}