package com.example.loomcraftadmin.ui.features.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loomcraftadmin.data.local.TokenManager
import com.example.loomcraftadmin.data.repository.AuthRepository
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    val rememberMe: StateFlow<Boolean> = tokenManager.rememberMe.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = true
    )

    val rememberedEmail: StateFlow<String> = tokenManager.rememberedEmail.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ""
    )

    fun login(email: String, password: String, rememberMe: Boolean) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            val result = authRepository.login(email, password, rememberMe)
            if (result.isSuccess) {
                tokenManager.saveRememberMe(rememberMe)
                tokenManager.saveRememberedEmail(if (rememberMe) email else "")
                try {
                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val token = task.result
                            viewModelScope.launch {
                                authRepository.registerFcmToken(token)
                                tokenManager.saveFcmToken(token)
                            }
                        }
                    }
                } catch (_: Exception) {
                    // Ignore FCM registration failures during login.
                }
                _uiState.value = LoginUiState.Success
            } else {
                _uiState.value = LoginUiState.Error(
                    result.exceptionOrNull()?.message ?: "Login failed"
                )
            }
        }
    }
}

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
