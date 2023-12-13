package com.example.preteirb.api

import com.example.preteirb.data.item.Item
import com.example.preteirb.data.item.ItemAndUsages
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ItemApiService {
    @GET("items/")
    suspend fun getItems(): List<Item>

    @GET("items/{id}")
    suspend fun getItem(@Path("id") id: Int): Item

    @DELETE("items/{id}")
    suspend fun deleteItem(@Path("id") id: Int)

    @PATCH("items/{id}")
    suspend fun updateItem(@Path("id") id: Int, item: Item)

    @POST("items/")
    suspend fun createItem(@Body item: Item): Item

    @GET("items/{id}/usages")
    suspend fun getUsagesByItem(@Path("id") id: Int): ItemAndUsages
}