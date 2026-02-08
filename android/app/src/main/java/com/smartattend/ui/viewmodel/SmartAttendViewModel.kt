package com.smartattend.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartattend.data.AttendanceItem
import com.smartattend.data.AttendanceRequest
import com.smartattend.data.AttendanceResponse
import com.smartattend.data.ClassResponse
import com.smartattend.data.DashboardResponse
import com.smartattend.data.DateReport
import com.smartattend.data.SessionManager
import com.smartattend.data.SmartAttendRepository
import com.smartattend.data.StudentReport
import com.smartattend.data.StudentRequest
import com.smartattend.data.StudentResponse
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
    private val repository: SmartAttendRepository = SmartAttendRepository(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        SmartAttendUiState(
            userName = SessionManager.currentUser.value?.fullName,
            userEmail = SessionManager.currentUser.value?.email,
        ),
    )
    val uiState: StateFlow<SmartAttendUiState> = _uiState

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
                _uiState.update { it.copy(isLoading = false, errorMessage = ex.localizedMessage ?: "Unable to load dashboard") }
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
                _uiState.update { it.copy(isLoading = false, errorMessage = ex.localizedMessage ?: "Unable to load classes") }
            }
        }
    }

    fun createClass(name: String, description: String?, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                repository.createClass(name, description)
                loadClasses()
                onSuccess()
            } catch (ex: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = ex.localizedMessage ?: "Unable to create class") }
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
                _uiState.update { it.copy(isLoading = false, errorMessage = ex.localizedMessage ?: "Unable to load students") }
            }
        }
    }

    fun createStudent(request: StudentRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                repository.createStudent(request)
                loadStudents(request.classId)
                onSuccess()
            } catch (ex: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = ex.localizedMessage ?: "Unable to create student") }
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
                _uiState.update { it.copy(isLoading = false, errorMessage = ex.localizedMessage ?: "Unable to load attendance") }
            }
        }
    }

    fun saveAttendance(classId: Long, date: String, records: List<AttendanceItem>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                repository.saveAttendance(AttendanceRequest(classId, date, records))
                loadAttendance(classId, date)
                onSuccess()
            } catch (ex: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = ex.localizedMessage ?: "Unable to save attendance") }
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
                _uiState.update { it.copy(isLoading = false, errorMessage = ex.localizedMessage ?: "Unable to load reports") }
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
                _uiState.update { it.copy(isLoading = false, errorMessage = ex.localizedMessage ?: "Unable to load date reports") }
            }
        }
    }
}
