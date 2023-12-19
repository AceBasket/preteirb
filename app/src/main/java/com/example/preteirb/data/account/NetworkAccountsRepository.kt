package com.example.preteirb.data.account

import com.example.preteirb.api.AuthApiService
import javax.inject.Inject

class NetworkAccountsRepository @Inject constructor(private val authApiService: AuthApiService) :
    AccountsRepository {
    override suspend fun login(loginDto: LoginDto): LoginResponseDto =
        authApiService.login(loginDto)

    override suspend fun signUp(loginDto: LoginDto): LoginResponseDto = authApiService.signUp(loginDto)
}