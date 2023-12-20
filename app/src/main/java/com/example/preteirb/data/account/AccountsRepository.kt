package com.example.preteirb.data.account

import retrofit2.Response

interface AccountsRepository {
    suspend fun login(loginDto: LoginDto): Response<LoginResponseDto>
    suspend fun signUp(loginDto: LoginDto): Response<LoginResponseDto>
}