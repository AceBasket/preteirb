package com.example.preteirb.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.user.UsersRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class UserProfileViewModel(
    private val settingsRepository: SettingsRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {
    // set userProfileUiState to the current user's details by getting userId from settingsRepository then getting user from usersRepository
    val userProfileUiState: StateFlow<UserProfileUiState> = settingsRepository.getUserId()
        .flatMapLatest { userId ->
            usersRepository.getUserStream(userId)
        }
        .filterNotNull()
        .map { user ->
            UserProfileUiState(user.toUserDetails())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = UserProfileUiState()
        )
    
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class UserProfileUiState(
    val userDetails: UserDetails = UserDetails()
)