package com.smartattend.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartattend.data.AttendanceItem
import com.smartattend.data.AttendanceRequest
import com.smartattend.data.AttendanceResponse
import android.content.Context
import com.smartattend.data.ClassResponse
import com.smartattend.data.DashboardResponse
import com.smartattend.data.DateReport
import com.smartattend.data.SessionManager
import com.smartattend.data.ServiceLocator
import com.smartattend.data.SmartAttendRepository
import com.smartattend.data.StudentReport
import com.smartattend.data.StudentRequest
import com.smartattend.data.StudentResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SmartAttendUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val dashboard: DashboardResponse? = null,
    val classes: List<ClassResponse> = emptyList(),
    val students: List<StudentResponse> = emptyList(),
    val attendance: List<AttendanceResponse> = emptyList(),
    val studentReports: List<StudentReport> = emptyList(),
    val dateReports: List<DateReport> = emptyList(),
    val userName: String? = null,
    val userEmail: String? = null,
)

class SmartAttendViewModel(
    private val repository: SmartAttendRepository = ServiceLocator.repository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        SmartAttendUiState(
            userName = SessionManager.currentUser.value?.fullName,
            userEmail = SessionManager.currentUser.value?.email,
        ),
    )
    val uiState: StateFlow<SmartAttendUiState> = _uiState
    private val _events = MutableSharedFlow<UiEvent>()
    val events: SharedFlow<UiEvent> = _events

    fun refreshAll() {
        loadDashboard()
        loadClasses()
        loadStudents(null)
        loadStudentReports(null)
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = repository.fetchDashboard()
                _uiState.update { it.copy(isLoading = false, dashboard = response) }
            } catch (ex: Exception) {
                val message = ex.localizedMessage ?: "Unable to load dashboard"
                _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                _events.emit(UiEvent.Error(message))
            }
        }
    }

    fun loadClasses() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = repository.fetchClasses()
                _uiState.update { it.copy(isLoading = false, classes = response) }
            } catch (ex: Exception) {
                val message = ex.localizedMessage ?: "Unable to load classes"
                _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                _events.emit(UiEvent.Error(message))
            }
        }
    }

    fun createClass(name: String, description: String?, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = repository.createClass(name, description)
                loadClasses()
                onSuccess()
                if (response.createdAt == "offline") {
                    _events.emit(UiEvent.Offline("Class saved offline and will sync later."))
                } else {
                    _events.emit(UiEvent.Success("Class created successfully."))
                }
            } catch (ex: Exception) {
                val message = ex.localizedMessage ?: "Unable to create class"
                _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                _events.emit(UiEvent.Error(message))
            }
        }
    }

    fun updateClass(id: Long, name: String, description: String?, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = repository.updateClass(id, name, description)
                loadClasses()
                onSuccess()
                if (response.createdAt == "offline") {
                    _events.emit(UiEvent.Offline("Class updated offline and will sync later."))
                } else {
                    _events.emit(UiEvent.Success("Class updated successfully."))
                }
            } catch (ex: Exception) {
                val message = ex.localizedMessage ?: "Unable to update class"
                _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                _events.emit(UiEvent.Error(message))
            }
        }
    }

    fun deleteClass(id: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                repository.deleteClass(id)
                loadClasses()
                onSuccess()
                _events.emit(UiEvent.Success("Class deleted successfully."))
            } catch (ex: Exception) {
                val message = ex.localizedMessage ?: "Unable to delete class"
                _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                _events.emit(UiEvent.Error(message))
            }
        }
    }

    fun loadStudents(classId: Long?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = repository.fetchStudents(classId)
                _uiState.update { it.copy(isLoading = false, students = response) }
            } catch (ex: Exception) {
                val message = ex.localizedMessage ?: "Unable to load students"
                _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                _events.emit(UiEvent.Error(message))
            }
        }
    }

    fun createStudent(request: StudentRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = repository.createStudent(request)
                loadStudents(request.classId)
                onSuccess()
                if (response.createdAt == "offline") {
                    _events.emit(UiEvent.Offline("Student saved offline and will sync later."))
                } else {
                    _events.emit(UiEvent.Success("Student added successfully."))
                }
            } catch (ex: Exception) {
                val message = ex.localizedMessage ?: "Unable to create student"
                _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                _events.emit(UiEvent.Error(message))
            }
        }
    }

    fun updateStudent(id: Long, request: StudentRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = repository.updateStudent(id, request)
                loadStudents(request.classId)
                onSuccess()
                if (response.createdAt == "offline") {
                    _events.emit(UiEvent.Offline("Student updated offline and will sync later."))
                } else {
                    _events.emit(UiEvent.Success("Student updated successfully."))
                }
            } catch (ex: Exception) {
                val message = ex.localizedMessage ?: "Unable to update student"
                _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                _events.emit(UiEvent.Error(message))
            }
        }
    }

    fun deleteStudent(id: Long, classId: Long?, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                repository.deleteStudent(id)
                loadStudents(classId)
                onSuccess()
                _events.emit(UiEvent.Success("Student deleted successfully."))
            } catch (ex: Exception) {
                val message = ex.localizedMessage ?: "Unable to delete student"
                _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                _events.emit(UiEvent.Error(message))
            }
        }
    }

    fun loadAttendance(classId: Long, date: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = repository.fetchAttendance(classId, date)
                _uiState.update { it.copy(isLoading = false, attendance = response) }
            } catch (ex: Exception) {
                val message = ex.localizedMessage ?: "Unable to load attendance"
                _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                _events.emit(UiEvent.Error(message))
            }
        }
    }

    fun saveAttendance(classId: Long, date: String, records: List<AttendanceItem>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = repository.saveAttendance(AttendanceRequest(classId, date, records))
                loadAttendance(classId, date)
                onSuccess()
                if (response.firstOrNull()?.className == "Offline") {
                    _events.emit(UiEvent.Offline("Attendance saved offline and will sync later."))
                } else {
                    _events.emit(UiEvent.Success("Attendance saved successfully."))
                }
            } catch (ex: Exception) {
                val message = ex.localizedMessage ?: "Unable to save attendance"
                _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                _events.emit(UiEvent.Error(message))
            }
        }
    }

    fun loadStudentReports(classId: Long?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = repository.fetchStudentReports(classId)
                _uiState.update { it.copy(isLoading = false, studentReports = response) }
            } catch (ex: Exception) {
                val message = ex.localizedMessage ?: "Unable to load reports"
                _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                _events.emit(UiEvent.Error(message))
            }
        }
    }

    fun loadDateReports(classId: Long, date: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = repository.fetchDateReports(classId, date)
                _uiState.update { it.copy(isLoading = false, dateReports = response) }
            } catch (ex: Exception) {
                val message = ex.localizedMessage ?: "Unable to load date reports"
                _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                _events.emit(UiEvent.Error(message))
            }
        }
    }

    fun updateProfile(context: Context, name: String, email: String) {
        val userId = SessionManager.userId ?: return
        val updated = com.smartattend.data.User(userId, name, email)
        SessionManager.updateUser(context, updated)
        _uiState.update { it.copy(userName = name, userEmail = email) }
    }
}
