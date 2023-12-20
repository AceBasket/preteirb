package com.example.preteirb.api

import com.example.preteirb.data.usage.UsageDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface UsageApiService {
    @GET("usages/")
    suspend fun getUsages(): List<UsageDto>

    @GET("usages/{id}/")
    suspend fun getUsage(@Path("id") id: Int): UsageDto

    @PATCH("usages/{id}/")
    suspend fun updateUsage(@Path("id") id: Int, usage: UsageDto)

    @DELETE("usages/{id}/")
    suspend fun deleteUsage(@Path("id") id: Int)

    @POST("usages/")
    suspend fun createUsage(@Body usage: UsageDto): UsageDto
}