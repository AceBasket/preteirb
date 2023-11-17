package com.example.preteirb.di

import android.content.Context
import com.example.preteirb.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
    
    @Provides
    fun provideItemDao(appDatabase: AppDatabase) = appDatabase.itemDao()
    
    @Provides
    fun provideUsageDao(appDatabase: AppDatabase) = appDatabase.usageDao()
    
    @Provides
    fun provideUserDao(appDatabase: AppDatabase) = appDatabase.userDao()
}