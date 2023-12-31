package com.example.preteirb.di

import com.example.preteirb.BuildConfig
import com.example.preteirb.api.AuthApiService
import com.example.preteirb.api.AuthInterceptor
import com.example.preteirb.api.ItemApiService
import com.example.preteirb.api.ProfileApiService
import com.example.preteirb.api.UsageApiService
import com.example.preteirb.common.Constants
import com.example.preteirb.data.cache.current_user.CurrentUserRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor) = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(currentUserRepository: CurrentUserRepository): AuthInterceptor =
        AuthInterceptor(currentUserRepository)

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideProfileApiService(retrofit: Retrofit) =
        retrofit.create(ProfileApiService::class.java)

    @Provides
    @Singleton
    fun provideItemApiService(retrofit: Retrofit) =
        retrofit.create(ItemApiService::class.java)

    @Provides
    @Singleton
    fun provideUsageApiService(retrofit: Retrofit) =
        retrofit.create(UsageApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit) =
        retrofit.create(AuthApiService::class.java)

}