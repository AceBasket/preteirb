package com.example.preteirb.data.account

interface AccountsRepository {
    suspend fun login(loginDto: LoginDto): LoginResponseDto
    suspend fun signUp(loginDto: LoginDto): LoginResponseDto
}