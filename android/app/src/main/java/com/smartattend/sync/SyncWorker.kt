package com.smartattend.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.smartattend.data.AttendanceRequest
import com.smartattend.data.ClassRequest
import com.smartattend.data.PendingActionType
import com.smartattend.data.ServiceLocator
import com.smartattend.data.StudentRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class SyncWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val api = com.smartattend.data.ApiClient.api
        val dao = ServiceLocator.database.smartAttendDao()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val classAdapter = moshi.adapter(ClassRequest::class.java)
        val studentAdapter = moshi.adapter(StudentRequest::class.java)
        val attendanceAdapter = moshi.adapter(AttendanceRequest::class.java)

        val pending = dao.getPendingActions()
        for (action in pending) {
            try {
                when (PendingActionType.valueOf(action.type)) {
                    PendingActionType.CREATE_CLASS -> {
                        val request = classAdapter.fromJson(action.payload) ?: continue
                        api.createClass(request)
                    }
                    PendingActionType.CREATE_STUDENT -> {
                        val request = studentAdapter.fromJson(action.payload) ?: continue
                        api.createStudent(request)
                    }
                    PendingActionType.SAVE_ATTENDANCE -> {
                        val request = attendanceAdapter.fromJson(action.payload) ?: continue
                        api.saveAttendance(request)
                    }
                }
                dao.deletePendingAction(action.id)
            } catch (_: Exception) {
                return Result.retry()
            }
        }
        return Result.success()
    }
}
