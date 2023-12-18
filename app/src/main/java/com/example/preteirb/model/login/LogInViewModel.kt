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
    var loginState by mutableStateOf(LoginState())

    fun updateUiState(loginDetails: LoginDetails) {
        loginState = loginState.copy(loginDetails = loginDetails)
    }

    suspend fun login() {
        val loginResponse = accountsRepository.login(loginState.loginDetails.toLoginDto())
        settingsRepository.saveToken(loginResponse.token)
    }
}

data class LoginState(
    val loginDetails: LoginDetails = LoginDetails()
)

data class LoginDetails(
    val username: String = "",
    val password: String = "",
)

fun LoginDetails.toLoginDto() = LoginDto(
    username = username,
    password = password,
)