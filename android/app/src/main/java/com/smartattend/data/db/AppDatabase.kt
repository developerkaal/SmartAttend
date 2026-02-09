package com.smartattend.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ClassEntity::class,
        StudentEntity::class,
        AttendanceEntity::class,
        StudentReportEntity::class,
        DateReportEntity::class,
        PendingActionEntity::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun smartAttendDao(): SmartAttendDao
}
