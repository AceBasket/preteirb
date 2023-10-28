package com.example.preteirb.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.preteirb.data.item.ItemsRepository
import com.example.preteirb.data.item.OfflineItemsRepository
import com.example.preteirb.data.usage.OfflineUsagesRepository
import com.example.preteirb.data.usage.UsagesRepository
import com.example.preteirb.data.user.OfflineUsersRepository
import com.example.preteirb.data.user.UsersRepository

private const val USER_PREFERENCES_NAME = "user_preferences"

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val usersRepository: UsersRepository
    val itemsRepository: ItemsRepository
    val usagesRepository: UsagesRepository
    val settingsRepository: SettingsRepository
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
    
    /**
     * Implementation for [SettingsRepository]
     */
    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME,
    )
    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(context.dataStore)
    }
}