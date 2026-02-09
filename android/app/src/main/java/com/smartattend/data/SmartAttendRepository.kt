package com.smartattend.data

import com.smartattend.data.db.AttendanceEntity
import com.smartattend.data.db.ClassEntity
import com.smartattend.data.db.DateReportEntity
import com.smartattend.data.db.PendingActionEntity
import com.smartattend.data.db.SmartAttendDao
import com.smartattend.data.db.StudentEntity
import com.smartattend.data.db.StudentReportEntity
import com.smartattend.sync.SyncScheduler
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class SmartAttendRepository(
    private val api: SmartAttendApi = ApiClient.api,
    private val dao: SmartAttendDao,
) {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val classAdapter = moshi.adapter(ClassRequest::class.java)
    private val studentAdapter = moshi.adapter(StudentRequest::class.java)
    private val attendanceAdapter = moshi.adapter(AttendanceRequest::class.java)

    suspend fun login(email: String, password: String): AuthResponse {
        return api.login(LoginRequest(email = email, password = password))
    }

    suspend fun register(fullName: String, email: String, password: String): AuthResponse {
        return api.register(RegisterRequest(fullName = fullName, email = email, password = password))
    }

    suspend fun fetchDashboard(): DashboardResponse = api.dashboard()

    suspend fun fetchClasses(): List<ClassResponse> {
        return runCatching { api.classes() }
            .onSuccess { response ->
                dao.clearClasses()
                dao.upsertClasses(response.map { it.toEntity() })
            }
            .getOrElse {
                dao.getClasses().map { it.toModel() }
            }
    }

    suspend fun createClass(name: String, description: String?): ClassResponse {
        return runCatching { api.createClass(ClassRequest(name = name, description = description)) }
            .onSuccess { response ->
                dao.upsertClasses(listOf(response.toEntity()))
            }
            .getOrElse {
                val payload = classAdapter.toJson(ClassRequest(name, description))
                dao.insertPendingAction(
                    PendingActionEntity(
                        type = PendingActionType.CREATE_CLASS.name,
                        payload = payload,
                        createdAt = System.currentTimeMillis(),
                    ),
                )
                SyncScheduler.enqueue()
                val fallback = ClassResponse(
                    id = System.currentTimeMillis(),
                    name = name,
                    description = description,
                    createdAt = "offline",
                    studentCount = 0,
                )
                dao.upsertClasses(listOf(fallback.toEntity()))
                fallback
            }
    }

    suspend fun fetchStudents(classId: Long?): List<StudentResponse> {
        return runCatching { api.students(classId) }
            .onSuccess { response ->
                dao.clearStudents()
                dao.upsertStudents(response.map { it.toEntity() })
            }
            .getOrElse {
                dao.getStudents(classId).map { it.toModel() }
            }
    }

    suspend fun createStudent(request: StudentRequest): StudentResponse {
        return runCatching { api.createStudent(request) }
            .onSuccess { response ->
                dao.upsertStudents(listOf(response.toEntity()))
            }
            .getOrElse {
                val payload = studentAdapter.toJson(request)
                dao.insertPendingAction(
                    PendingActionEntity(
                        type = PendingActionType.CREATE_STUDENT.name,
                        payload = payload,
                        createdAt = System.currentTimeMillis(),
                    ),
                )
                SyncScheduler.enqueue()
                val fallback = StudentResponse(
                    id = System.currentTimeMillis(),
                    rollNo = request.rollNo,
                    fullName = request.fullName,
                    email = request.email,
                    phone = request.phone,
                    classId = request.classId,
                    className = "Offline",
                    createdAt = "offline",
                )
                dao.upsertStudents(listOf(fallback.toEntity()))
                fallback
            }
    }

    suspend fun fetchAttendance(classId: Long, date: String): List<AttendanceResponse> {
        return runCatching { api.attendance(classId, date) }
            .onSuccess { response ->
                dao.clearAttendance(classId, date)
                dao.upsertAttendance(response.map { it.toEntity() })
            }
            .getOrElse {
                dao.getAttendance(classId, date).map { it.toModel() }
            }
    }

    suspend fun saveAttendance(request: AttendanceRequest): List<AttendanceResponse> {
        return runCatching { api.saveAttendance(request) }
            .onSuccess { response ->
                dao.clearAttendance(request.classId, request.date)
                dao.upsertAttendance(response.map { it.toEntity() })
            }
            .getOrElse {
                val payload = attendanceAdapter.toJson(request)
                dao.insertPendingAction(
                    PendingActionEntity(
                        type = PendingActionType.SAVE_ATTENDANCE.name,
                        payload = payload,
                        createdAt = System.currentTimeMillis(),
                    ),
                )
                SyncScheduler.enqueue()
                val fallback = request.records.mapIndexed { index, record ->
                    AttendanceResponse(
                        id = System.currentTimeMillis() + index,
                        studentId = record.studentId,
                        studentName = "Offline",
                        classId = request.classId,
                        className = "Offline",
                        date = request.date,
                        present = record.present,
                    )
                }
                dao.clearAttendance(request.classId, request.date)
                dao.upsertAttendance(fallback.map { it.toEntity() })
                fallback
            }
    }

    suspend fun fetchStudentReports(classId: Long?): List<StudentReport> {
        return runCatching { api.studentReports(classId = classId) }
            .onSuccess { response ->
                dao.clearStudentReports()
                dao.upsertStudentReports(response.map { it.toEntity() })
            }
            .getOrElse {
                dao.getStudentReports().map { it.toModel() }
            }
    }

    suspend fun fetchDateReports(classId: Long, date: String): List<DateReport> {
        return runCatching { api.dateReports(classId, date) }
            .onSuccess { response ->
                dao.clearDateReports(date)
                dao.upsertDateReports(response.map { it.toEntity(date) })
            }
            .getOrElse {
                dao.getDateReports(date, null).map { it.toModel() }
            }
    }
}

enum class PendingActionType { CREATE_CLASS, CREATE_STUDENT, SAVE_ATTENDANCE }

private fun ClassResponse.toEntity() = ClassEntity(id, name, description, createdAt, studentCount)
private fun ClassEntity.toModel() = ClassResponse(id, name, description, createdAt, studentCount)

private fun StudentResponse.toEntity() = StudentEntity(id, rollNo, fullName, email, phone, classId, className, createdAt)
private fun StudentEntity.toModel() = StudentResponse(id, rollNo, fullName, email, phone, classId, className, createdAt)

private fun AttendanceResponse.toEntity() = AttendanceEntity(id, studentId, studentName, classId, className, date, present)
private fun AttendanceEntity.toModel() = AttendanceResponse(id, studentId, studentName, classId, className, date, present)

private fun StudentReport.toEntity() = StudentReportEntity(
    studentId,
    rollNo,
    fullName,
    className,
    totalDays,
    presentDays,
    absentDays,
    percentage,
)

private fun StudentReportEntity.toModel() = StudentReport(
    studentId,
    rollNo,
    fullName,
    className,
    totalDays,
    presentDays,
    absentDays,
    percentage,
)

private fun DateReport.toEntity(date: String) = DateReportEntity(
    studentId,
    rollNo,
    fullName,
    present,
    className,
    date,
)

private fun DateReportEntity.toModel() = DateReport(
    studentId,
    rollNo,
    fullName,
    present,
    className,
)
