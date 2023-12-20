package com.example.preteirb.model.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.preteirb.common.snackbar.SnackbarManager
import com.example.preteirb.data.account.AccountsRepository
import com.example.preteirb.data.account.LoginDto
import com.example.preteirb.data.cache.current_user.CurrentUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val currentUserRepository: CurrentUserRepository,
    private val accountsRepository: AccountsRepository
) : ViewModel() {
    var uiState by mutableStateOf(AuthState())
        private set

    fun updateUiState(authDetails: AuthDetails) {
        uiState =
            uiState.copy(authDetails = authDetails, isEntryValid = validateInput(authDetails))
    }

    fun setAccountCreation(isAccountCreation: Boolean) {
        uiState = uiState.copy(isAccountCreation = isAccountCreation)
    }

    suspend fun login() {
        authenticate(true)
    }

    private suspend fun authenticate(isLogin: Boolean) {
        if (!uiState.isEntryValid) return
        uiState = uiState.copy(isAuthLoading = true)
        val authResponse = if (isLogin) {
            accountsRepository.login(uiState.authDetails.toLoginDto())
        } else {
            accountsRepository.signUp(uiState.authDetails.toLoginDto())
        }
        if (authResponse.isSuccessful) {
            authResponse.body()?.token?.let {
                currentUserRepository.setToken(it)
                uiState = uiState.copy(isAuthSuccess = true)
                currentUserRepository.setIsLoggedIn(true)
            } ?: run {
                SnackbarManager.showMessage("No token found")
                uiState = uiState.copy(isAuthLoading = false)
            }
        } else {
            val error = authResponse.errorBody()?.string()
            val jsonError = JSONObject(error ?: "{}")
            jsonError.keys().forEach {
                SnackbarManager.showMessage(it + ": " + jsonError.getString(it))
            }
            uiState = uiState.copy(isAuthLoading = false)
        }
    }

    suspend fun signUp() {
        if (!isPasswordValid()) {
            SnackbarManager.showMessage("Passwords do not match")
            return
        }
        authenticate(false)
    }

    private fun validateInput(authDetails: AuthDetails): Boolean {
        return authDetails.username.isNotBlank() && authDetails.password.isNotBlank() //&& (!uiState.isAccountCreation || isPasswordValid())
    }

    private fun isPasswordValid(): Boolean {
        return uiState.authDetails.password == uiState.authDetails.passwordConfirmation
    }
}

data class AuthState(
    val authDetails: AuthDetails = AuthDetails(),
    val isAccountCreation: Boolean = false,
    val isEntryValid: Boolean = false,
    val isAuthLoading: Boolean = false,
    val isAuthSuccess: Boolean = false,
)

data class AuthDetails(
    val username: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
)

fun AuthDetails.toLoginDto() = LoginDto(
    username = username,
    password = password,
)