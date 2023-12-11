package com.example.preteirb.api

import com.example.preteirb.data.item.Item
import com.example.preteirb.data.item.ItemAndUsages
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ItemApiService {
    @GET("items")
    suspend fun getItems(): List<Item>

    @GET("items/{id}")
    suspend fun getItem(id: Int): Item

    @DELETE("items/{id}")
    suspend fun deleteItem(id: Int)

    @PATCH("items/{id}")
    suspend fun updateItem(id: Int, item: Item)

    @POST("items")
    suspend fun createItem(item: Item): Long

    @GET("items/{id}/usages")
    suspend fun getUsagesByItem(id: Int): ItemAndUsages
}