package com.smartattend.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore("session")

object SessionManager {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val keyUserId = longPreferencesKey("user_id")
    private val keyFullName = stringPreferencesKey("full_name")
    private val keyEmail = stringPreferencesKey("email")

    fun init(context: Context) {
        scope.launch {
            val prefs = context.sessionDataStore.data.firstOrNull()
            val userId = prefs?.get(keyUserId)
            val fullName = prefs?.get(keyFullName)
            val email = prefs?.get(keyEmail)
            if (userId != null && fullName != null && email != null) {
                _currentUser.value = User(userId, fullName, email)
            }
        }
    }

    val userId: Long?
        get() = _currentUser.value?.id

    fun updateUser(context: Context, user: User) {
        _currentUser.value = user
        scope.launch {
            context.sessionDataStore.edit { prefs ->
                prefs[keyUserId] = user.id
                prefs[keyFullName] = user.fullName
                prefs[keyEmail] = user.email
            }
        }
    }

    fun clear(context: Context) {
        _currentUser.value = null
        scope.launch {
            context.sessionDataStore.edit { prefs ->
                prefs.remove(keyUserId)
                prefs.remove(keyFullName)
                prefs.remove(keyEmail)
            }
        }
    }
}
