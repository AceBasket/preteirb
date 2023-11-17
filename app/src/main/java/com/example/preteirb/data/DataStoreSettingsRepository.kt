package com.example.preteirb.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.preteirb.data.DataStoreSettingsRepository.PreferencesKeys.USER_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreSettingsRepository @Inject constructor(private val dataStore: DataStore<Preferences>) :
    SettingsRepository {
    private object PreferencesKeys {
        val USER_ID = intPreferencesKey("user_id")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }
    
    override suspend fun storeUserId(userId: Int) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }
    
    override suspend fun storeIsLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = isLoggedIn
        }
    }
    
    override fun getUserId(): Flow<Int> = dataStore.data.map { preferences ->
        preferences[USER_ID] ?: 0
    }
    
    override fun getIsLoggedIn(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_LOGGED_IN] ?: false
    }
}