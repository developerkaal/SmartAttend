package com.smartattend.data

import android.content.Context
import androidx.room.Room
import com.smartattend.data.db.AppDatabase

object ServiceLocator {
    private var initialized = false
    lateinit var appContext: Context
        private set
    lateinit var database: AppDatabase
        private set
    lateinit var repository: SmartAttendRepository
        private set

    fun init(context: Context) {
        if (initialized) return
        appContext = context.applicationContext
        database = Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "smartattend.db",
        ).build()
        repository = SmartAttendRepository(
            api = ApiClient.api,
            dao = database.smartAttendDao(),
        )
        initialized = true
    }
}
