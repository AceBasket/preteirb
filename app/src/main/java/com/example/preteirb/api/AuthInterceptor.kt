package com.example.preteirb.api

import com.example.preteirb.data.cache.current_user.CurrentUserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val currentUserRepository: CurrentUserRepository) :
    Interceptor {
    /**
     * Interceptor class for setting of the headers for every request
     */
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val requestBuilder = chain.request().newBuilder()
        val token = runBlocking { currentUserRepository.currentUserFlow.first().token }
        if (token.isNotBlank()) {
            requestBuilder.addHeader("Authorization", "Token $token")
        }
        return chain.proceed(requestBuilder.build())
    }
}