package com.smartattend.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartattend.data.SessionManager
import com.smartattend.data.SmartAttendRepository
import com.smartattend.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLogin: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class AuthViewModel(
    private val repository: SmartAttendRepository = SmartAttendRepository(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun toggleMode() {
        _uiState.update { it.copy(isLogin = !it.isLogin, errorMessage = null) }
    }

    fun submit(
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
                    SessionManager.updateUser(User(user.id, user.fullName, user.email))
                    onSuccess()
                    _uiState.update { it.copy(isLoading = false) }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = response.message) }
                }
            } catch (ex: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = ex.localizedMessage ?: "Something went wrong") }
            }
        }
    }
}
