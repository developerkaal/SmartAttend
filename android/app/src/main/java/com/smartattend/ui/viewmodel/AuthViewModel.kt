package com.smartattend.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartattend.data.SessionManager
import com.smartattend.data.SmartAttendRepository
import com.smartattend.data.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class UiEvent {
    data class Success(val message: String) : UiEvent()
    data class Error(val message: String) : UiEvent()
    data class Offline(val message: String) : UiEvent()
}

data class AuthUiState(
    val isLogin: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class AuthViewModel(
    private val repository: SmartAttendRepository = com.smartattend.data.ServiceLocator.repository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState
    private val _events = MutableSharedFlow<UiEvent>()
    val events: SharedFlow<UiEvent> = _events

    fun toggleMode() {
        _uiState.update { it.copy(isLogin = !it.isLogin, errorMessage = null) }
    }

    fun submit(
        context: Context,
        fullName: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = if (_uiState.value.isLogin) {
                    repository.login(email, password)
                } else {
                    repository.register(fullName, email, password)
                }
                val user = response.user
                if (user != null) {
                    SessionManager.updateUser(context, User(user.id, user.fullName, user.email))
                    onSuccess()
                    _uiState.update { it.copy(isLoading = false) }
                    _events.emit(UiEvent.Success(if (_uiState.value.isLogin) "Login successful" else "Account created successfully"))
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = response.message) }
                    _events.emit(UiEvent.Error(response.message))
                }
            } catch (ex: Exception) {
                val message = ex.localizedMessage ?: "Something went wrong"
                _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                _events.emit(UiEvent.Error(message))
            }
        }
    }
}
