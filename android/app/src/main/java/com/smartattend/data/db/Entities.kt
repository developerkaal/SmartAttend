package com.smartattend.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classes")
data class ClassEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String?,
    val createdAt: String,
    val studentCount: Int,
)

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey val id: Long,
    val rollNo: String,
    val fullName: String,
    val email: String?,
    val phone: String?,
    val classId: Long,
    val className: String,
    val createdAt: String,
)

@Entity(tableName = "attendance")
data class AttendanceEntity(
    @PrimaryKey val id: Long,
    val studentId: Long,
    val studentName: String,
    val classId: Long,
    val className: String,
    val date: String,
    val present: Boolean,
)

@Entity(tableName = "student_reports")
data class StudentReportEntity(
    @PrimaryKey val studentId: Long,
    val rollNo: String,
    val fullName: String,
    val className: String,
    val totalDays: Int,
    val presentDays: Int,
    val absentDays: Int,
    val percentage: Int,
)

@Entity(tableName = "date_reports", primaryKeys = ["studentId", "date"])
data class DateReportEntity(
    val studentId: Long,
    val rollNo: String,
    val fullName: String,
    val present: Boolean,
    val className: String,
    val date: String,
)

@Entity(tableName = "pending_actions")
data class PendingActionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val payload: String,
    val createdAt: Long,
)
