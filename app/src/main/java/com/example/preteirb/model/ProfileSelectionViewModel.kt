package com.example.preteirb.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.user.User
import com.example.preteirb.data.user.UsersRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all users in the Room database.
 */
class ProfileSelectionViewModel(
    private val usersRepository: UsersRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    val uiState: StateFlow<ProfileSelectionUiState> = usersRepository.getAllUsersStream()
        .map { users -> ProfileSelectionUiState(users) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ProfileSelectionUiState(),
        )
    
    suspend fun logIn(user: User) {
        settingsRepository.storeUserId(user.userId)
        settingsRepository.storeIsLoggedIn(true)
    }
    
    private suspend fun registerUser(user: User) {
        usersRepository.insertUser(user)
    }
    
    suspend fun registerUserAndLogIn(user: User) {
        registerUser(user)
        logIn(user)
    }
    
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ProfileSelectionUiState(
    val users: List<User> = emptyList(),
)