package com.example.preteirb.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val usersRepository: UsersRepository
    val itemsRepository: ItemsRepository
    val usagesRepository: UsagesRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineUsersRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [UsersRepository]
     */
    override val usersRepository: UsersRepository by lazy {
        OfflineUsersRepository(
            PreteirbDatabase.getDatabase(context).userDao(),
        )
    }
    
    /**
     * Implementation for [ItemsRepository]
     */
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(
            PreteirbDatabase.getDatabase(context).itemDao(),
        )
    }
    
    /**
     * Implementation for [UsagesRepository]
     */
    override val usagesRepository: UsagesRepository by lazy {
        OfflineUsagesRepository(
            PreteirbDatabase.getDatabase(context).usageDao(),
        )
    }
}