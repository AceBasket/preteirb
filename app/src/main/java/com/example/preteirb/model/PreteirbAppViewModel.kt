package com.example.preteirb.model

import androidx.lifecycle.ViewModel
import com.example.preteirb.data.SettingsRepository

class PreteirbAppViewModel(settingsRepository: SettingsRepository) : ViewModel() {
    
    var profileId = settingsRepository.getUserId()
        private set
    
    val isLoggedIn = settingsRepository.getIsLoggedIn()
}