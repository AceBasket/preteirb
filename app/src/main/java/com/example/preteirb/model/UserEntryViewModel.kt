package com.example.preteirb.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.preteirb.data.user.User
import com.example.preteirb.data.user.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserEntryViewModel @Inject constructor(private val usersRepository: UsersRepository) : ViewModel() {
    /**
     * Holds current user ui state
     */
    var userUiState by mutableStateOf(UserUiState())
        private set
    
    /**
     * Updates the [userUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(userDetails: UserDetails) {
        userUiState =
            UserUiState(userDetails = userDetails, isEntryValid = validateInput(userDetails))
    }
    
    private fun validateInput(uiState: UserDetails = userUiState.userDetails): Boolean {
        return with(uiState) {
            username.isNotBlank() && location.isNotBlank()
        }
    }
    
    suspend fun saveUser() {
        if (validateInput()) {
            usersRepository.insertUser(userUiState.userDetails.toUser())
        }
    }
}

/**
 * Represents Ui State for an User.
 */
data class UserUiState(
    val userDetails: UserDetails = UserDetails(),
    val isEntryValid: Boolean = false
)

data class UserDetails(
    val id: Int = 0,
    val username: String = "",
    val location: String = "",
)

fun UserDetails.toUser(): User = User(
    userId = id,
    username = username,
    location = location
)

fun User.toUserUiState(isEntryValid: Boolean = false): UserUiState = UserUiState(
    userDetails = UserDetails(
        id = userId,
        username = username,
        location = location
    ),
    isEntryValid = isEntryValid
)

fun User.toUserDetails(): UserDetails = UserDetails(
    id = userId,
    username = username,
    location = location
)

