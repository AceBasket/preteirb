package com.example.preteirb.di

import android.content.Context
import com.example.preteirb.data.cache.CacheDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CacheModule {
    @Singleton
    @Provides
    fun provideCacheDatabase(@ApplicationContext context: Context): CacheDatabase {
        return CacheDatabase.getDatabase(context)
    }

    @Provides
    fun provideItemOwnedDao(cacheDatabase: CacheDatabase) = cacheDatabase.itemOwnedDao()
}