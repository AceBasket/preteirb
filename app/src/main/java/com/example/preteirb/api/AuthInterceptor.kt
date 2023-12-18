package com.example.preteirb.api

import com.example.preteirb.data.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import javax.inject.Inject

class AuthInterceptor @Inject constructor(val settingsRepository: SettingsRepository) :
    Interceptor {
    /**
     * Interceptor class for setting of the headers for every request
     */
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val requestBuilder = chain.request().newBuilder()
        val token = runBlocking { settingsRepository.getToken().first() }
        if (token != null) {
            requestBuilder.addHeader("Authorization", "Token $token")
        }
        return chain.proceed(requestBuilder.build())
    }

//    /**
//     * Authenticator for when the authToken need to be refresh and updated
//     * everytime we get a 401 error code
//     */
//    @Throws(IOException::class)
//    override fun authenticate(route: Route?, response: Response?): Request? {
//        var requestAvailable: Request? = null
//        try {
//            requestAvailable = response?.request?.newBuilder()
//                ?.addHeader("AUTH_TOKEN", "UUID.randomUUID().toString()")
//                ?.build()
//            return requestAvailable
//        } catch (ex: Exception) {
//        }
//        return requestAvailable
//    }
}