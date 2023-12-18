package com.example.preteirb.api

import com.example.preteirb.data.account.LoginDto
import com.example.preteirb.data.account.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("account/login/")
    suspend fun login(@Body loginDto: LoginDto): LoginResponseDto
}