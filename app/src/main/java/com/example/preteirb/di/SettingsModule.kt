package com.example.preteirb.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.preteirb.data.DataStoreSettingsRepository
import com.example.preteirb.data.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val USER_PREFERENCES_NAME = "user_preferences"

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {
    companion object {
        @Singleton
        @Provides
        fun provideDataStorePreferences(@ApplicationContext applicationContext: Context): DataStore<Preferences> {
            return PreferenceDataStoreFactory.create(
                corruptionHandler = ReplaceFileCorruptionHandler(
                    produceNewData = { emptyPreferences() }
                ),
                migrations = listOf(
                    SharedPreferencesMigration(
                        applicationContext,
                        USER_PREFERENCES_NAME
                    )
                ),
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
                produceFile = {
                    applicationContext.preferencesDataStoreFile(USER_PREFERENCES_NAME)
                }
            )
        }
    }
    
    @Binds
    abstract fun bindSettingsRepository(dataStoreSettingsRepository: DataStoreSettingsRepository): SettingsRepository
}