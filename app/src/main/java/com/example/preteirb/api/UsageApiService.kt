package com.example.preteirb.api

import com.example.preteirb.data.usage.Usage
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UsageApiService {
    @GET("usages")
    suspend fun getUsages(): List<Usage>

    @GET("usages/{id}")
    suspend fun getUsage(id: Int): Usage

    @PATCH("usages/{id}")
    suspend fun updateUsage(id: Int, usage: Usage)

    @DELETE("usages/{id}")
    suspend fun deleteUsage(id: Int)

    @POST("usages")
    suspend fun createUsage(usage: Usage): Long
}