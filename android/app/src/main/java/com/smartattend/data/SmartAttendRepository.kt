package com.smartattend.data

class SmartAttendRepository(
    private val api: SmartAttendApi = ApiClient.api,
) {
    suspend fun login(email: String, password: String): AuthResponse {
        return api.login(LoginRequest(email = email, password = password))
    }

    suspend fun register(fullName: String, email: String, password: String): AuthResponse {
        return api.register(RegisterRequest(fullName = fullName, email = email, password = password))
    }

    suspend fun fetchDashboard(): DashboardResponse = api.dashboard()

    suspend fun fetchClasses(): List<ClassResponse> = api.classes()

    suspend fun createClass(name: String, description: String?): ClassResponse {
        return api.createClass(ClassRequest(name = name, description = description))
    }

    suspend fun fetchStudents(classId: Long?): List<StudentResponse> = api.students(classId)

    suspend fun createStudent(request: StudentRequest): StudentResponse = api.createStudent(request)

    suspend fun fetchAttendance(classId: Long, date: String): List<AttendanceResponse> {
        return api.attendance(classId, date)
    }

    suspend fun saveAttendance(request: AttendanceRequest): List<AttendanceResponse> {
        return api.saveAttendance(request)
    }

    suspend fun fetchStudentReports(classId: Long?): List<StudentReport> {
        return api.studentReports(classId = classId)
    }

    suspend fun fetchDateReports(classId: Long, date: String): List<DateReport> {
        return api.dateReports(classId, date)
    }
}
