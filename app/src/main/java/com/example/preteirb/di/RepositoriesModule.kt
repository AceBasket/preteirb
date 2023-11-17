package com.example.preteirb.di

import com.example.preteirb.data.item.ItemsRepository
import com.example.preteirb.data.item.OfflineItemsRepository
import com.example.preteirb.data.usage.OfflineUsagesRepository
import com.example.preteirb.data.usage.UsagesRepository
import com.example.preteirb.data.user.OfflineUsersRepository
import com.example.preteirb.data.user.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class) // SingletonComponent is a Dagger component that lives as long as the application lives.
abstract class RepositoriesModule {
    @Binds
    abstract fun bindItemsRepository(offlineItemsRepository: OfflineItemsRepository): ItemsRepository
    
    @Binds
    abstract fun bindUsagesRepository(offlineUsagesRepository: OfflineUsagesRepository): UsagesRepository
    
    @Binds
    abstract fun bindUsersRepository(offlineUsersRepository: OfflineUsersRepository): UsersRepository
}