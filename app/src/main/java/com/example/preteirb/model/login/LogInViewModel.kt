package com.example.preteirb.model.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.preteirb.data.SettingsRepository
import com.example.preteirb.data.account.AccountsRepository
import com.example.preteirb.data.account.LoginDto
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val accountsRepository: AccountsRepository
) : ViewModel() {
    private var _uiState by mutableStateOf(LoginState())

    val uiState: LoginState
        get() = _uiState

    fun updateUiState(loginDetails: LoginDetails) {
        _uiState =
            uiState.copy(loginDetails = loginDetails, isEntryValid = validateInput(loginDetails))
    }

    fun setAccountCreation(isAccountCreation: Boolean) {
        _uiState = uiState.copy(isAccountCreation = isAccountCreation)
    }

    suspend fun login() {
        if (!uiState.isEntryValid) return
        val loginResponse = accountsRepository.login(uiState.loginDetails.toLoginDto())
        settingsRepository.saveToken(loginResponse.token)
    }

    suspend fun signUp() {
        if (!uiState.isEntryValid) return
        val signUpResponse = accountsRepository.signUp(uiState.loginDetails.toLoginDto())
        settingsRepository.saveToken(signUpResponse.token)
    }

    private fun validateInput(loginDetails: LoginDetails): Boolean {
        return loginDetails.username.isNotBlank() && loginDetails.password.isNotBlank() && (!uiState.isAccountCreation || isPasswordValid())
    }

    fun isPasswordValid(): Boolean {
        return uiState.loginDetails.password == uiState.loginDetails.passwordConfirmation
    }
}

data class LoginState(
    val loginDetails: LoginDetails = LoginDetails(),
    val isAccountCreation: Boolean = false,
    val isEntryValid: Boolean = false,
)

data class LoginDetails(
    val username: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
)

fun LoginDetails.toLoginDto() = LoginDto(
    username = username,
    password = password,
)