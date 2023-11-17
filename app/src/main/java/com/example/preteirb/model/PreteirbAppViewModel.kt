package com.example.preteirb.model

import androidx.lifecycle.ViewModel
import com.example.preteirb.data.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreteirbAppViewModel @Inject constructor(settingsRepository: SettingsRepository) : ViewModel() {
    
    var profileId = settingsRepository.getUserId()
        private set
    
    val isLoggedIn = settingsRepository.getIsLoggedIn()
}