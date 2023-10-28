package com.example.preteirb.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preteirb.data.user.User
import com.example.preteirb.data.user.UsersRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all users in the Room database.
 */
class ChooseProfileViewModel(private val usersRepository: UsersRepository) : ViewModel() {
    
    val chooseProfileUiState: StateFlow<ChooseProfileUiState> =
        usersRepository.getAllUsersStream().map { ChooseProfileUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ChooseProfileUiState()
            )
    
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
    
}

data class ChooseProfileUiState(
    val userList: List<User> = listOf()
)