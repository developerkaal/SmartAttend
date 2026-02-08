package com.smartattend.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SessionManager {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    val userId: Long?
        get() = _currentUser.value?.id

    fun updateUser(user: User) {
        _currentUser.value = user
    }

    fun clear() {
        _currentUser.value = null
    }
}
