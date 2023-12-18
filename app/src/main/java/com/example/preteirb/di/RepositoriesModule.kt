package com.example.preteirb.di

import com.example.preteirb.data.DataStoreSettingsRepository
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.account.AccountsRepository
import com.example.preteirb.data.account.NetworkAccountsRepository
import com.example.preteirb.data.item.ItemsRepository
import com.example.preteirb.data.item.NetworkItemsRepository
import com.example.preteirb.data.usage.NetworkUsagesRepository
import com.example.preteirb.data.usage.UsagesRepository
import com.example.preteirb.data.user.NetworkUsersRepository
import com.example.preteirb.data.user.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class) // SingletonComponent is a Dagger component that lives as long as the application lives.
abstract class RepositoriesModule {
    @Binds
    abstract fun bindItemsRepository(networkItemsRepository: NetworkItemsRepository): ItemsRepository

    @Binds
    abstract fun bindUsagesRepository(networkUsagesRepository: NetworkUsagesRepository): UsagesRepository

    @Binds
    abstract fun bindUsersRepository(networkUsersRepository: NetworkUsersRepository): UsersRepository

    @Binds
    abstract fun bindSettingsRepository(dataStoreSettingsRepository: DataStoreSettingsRepository): SettingsRepository

    @Binds
    abstract fun bindAccountsRepository(networkAccountsRepository: NetworkAccountsRepository): AccountsRepository
}