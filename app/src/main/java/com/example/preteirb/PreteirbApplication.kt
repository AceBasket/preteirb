package com.example.preteirb

import android.app.Application
import com.example.preteirb.data.AppContainer
import com.example.preteirb.data.AppDataContainer

//private const val USER_PREFERENCES_NAME = "user_preferences"
//
//private val Context.dataStore by preferencesDataStore(
//    name = USER_PREFERENCES_NAME,
//)

class PreteirbApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer
    //lateinit var settingsRepository: SettingsRepository
    
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        //settingsRepository = SettingsRepository(dataStore)
    }
}