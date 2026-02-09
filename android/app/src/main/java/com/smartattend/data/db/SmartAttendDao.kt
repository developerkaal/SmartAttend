package com.smartattend.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SmartAttendDao {
    @Query("SELECT * FROM classes ORDER BY name")
    suspend fun getClasses(): List<ClassEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertClasses(items: List<ClassEntity>)

    @Query("DELETE FROM classes")
    suspend fun clearClasses()

    @Query("SELECT * FROM students WHERE (:classId IS NULL OR classId = :classId) ORDER BY fullName")
    suspend fun getStudents(classId: Long?): List<StudentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStudents(items: List<StudentEntity>)

    @Query("DELETE FROM students")
    suspend fun clearStudents()

    @Query("SELECT * FROM attendance WHERE classId = :classId AND date = :date")
    suspend fun getAttendance(classId: Long, date: String): List<AttendanceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAttendance(items: List<AttendanceEntity>)

    @Query("DELETE FROM attendance WHERE classId = :classId AND date = :date")
    suspend fun clearAttendance(classId: Long, date: String)

    @Query("SELECT * FROM student_reports ORDER BY fullName")
    suspend fun getStudentReports(): List<StudentReportEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStudentReports(items: List<StudentReportEntity>)

    @Query("DELETE FROM student_reports")
    suspend fun clearStudentReports()

    @Query("SELECT * FROM date_reports WHERE date = :date AND (:className IS NULL OR className = :className)")
    suspend fun getDateReports(date: String, className: String?): List<DateReportEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDateReports(items: List<DateReportEntity>)

    @Query("DELETE FROM date_reports WHERE date = :date")
    suspend fun clearDateReports(date: String)

    @Insert
    suspend fun insertPendingAction(action: PendingActionEntity)

    @Query("SELECT * FROM pending_actions ORDER BY createdAt ASC")
    suspend fun getPendingActions(): List<PendingActionEntity>

    @Query("DELETE FROM pending_actions WHERE id = :id")
    suspend fun deletePendingAction(id: Long)
}
