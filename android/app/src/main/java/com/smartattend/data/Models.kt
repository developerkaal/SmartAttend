package com.smartattend.data

data class AuthResponse(
    val user: User?,
    val message: String,
)

data class User(
    val id: Long,
    val fullName: String,
    val email: String,
)

data class LoginRequest(
    val email: String,
    val password: String,
)

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
)

data class DashboardResponse(
    val totalStudents: Long,
    val totalClasses: Long,
    val todayAttendance: Int,
    val overallAttendance: Int,
    val classSummaries: List<ClassSummary>,
)

data class ClassSummary(
    val classId: Long,
    val className: String,
    val present: Long,
    val total: Long,
    val percentage: Int,
)

data class ClassRequest(
    val name: String,
    val description: String?,
)

data class ClassResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val createdAt: String,
    val studentCount: Int,
)

data class StudentRequest(
    val rollNo: String,
    val fullName: String,
    val classId: Long,
    val email: String?,
    val phone: String?,
)

data class StudentResponse(
    val id: Long,
    val rollNo: String,
    val fullName: String,
    val email: String?,
    val phone: String?,
    val classId: Long,
    val className: String,
    val createdAt: String,
)

data class AttendanceRequest(
    val classId: Long,
    val date: String,
    val records: List<AttendanceItem>,
)

data class AttendanceItem(
    val studentId: Long,
    val present: Boolean,
)

data class AttendanceResponse(
    val id: Long,
    val studentId: Long,
    val studentName: String,
    val classId: Long,
    val className: String,
    val date: String,
    val present: Boolean,
)

data class StudentReport(
    val studentId: Long,
    val rollNo: String,
    val fullName: String,
    val className: String,
    val totalDays: Int,
    val presentDays: Int,
    val absentDays: Int,
    val percentage: Int,
)

data class DateReport(
    val studentId: Long,
    val rollNo: String,
    val fullName: String,
    val present: Boolean,
    val className: String,
)
