package com.example.preteirb

import android.app.Application
import com.example.preteirb.data.AppContainer
import com.example.preteirb.data.AppDataContainer

//@HiltAndroidApp
class PreteirbApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer
    
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}