package com.smartattend.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.smartattend.data.AttendanceRequest
import com.smartattend.data.ClassRequest
import com.smartattend.data.PendingActionType
import com.smartattend.data.DeleteRequest
import com.smartattend.data.ServiceLocator
import com.smartattend.data.UpdateClassRequest
import com.smartattend.data.UpdateStudentRequest
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
        val updateClassAdapter = moshi.adapter(UpdateClassRequest::class.java)
        val updateStudentAdapter = moshi.adapter(UpdateStudentRequest::class.java)
        val deleteAdapter = moshi.adapter(DeleteRequest::class.java)

        val pending = dao.getPendingActions()
        for (action in pending) {
            try {
                when (PendingActionType.valueOf(action.type)) {
                    PendingActionType.CREATE_CLASS -> {
                        val request = classAdapter.fromJson(action.payload) ?: continue
                        api.createClass(request)
                    }
                    PendingActionType.UPDATE_CLASS -> {
                        val request = updateClassAdapter.fromJson(action.payload) ?: continue
                        api.updateClass(request.id, ClassRequest(request.name, request.description))
                    }
                    PendingActionType.DELETE_CLASS -> {
                        val request = deleteAdapter.fromJson(action.payload) ?: continue
                        api.deleteClass(request.id)
                    }
                    PendingActionType.CREATE_STUDENT -> {
                        val request = studentAdapter.fromJson(action.payload) ?: continue
                        api.createStudent(request)
                    }
                    PendingActionType.UPDATE_STUDENT -> {
                        val request = updateStudentAdapter.fromJson(action.payload) ?: continue
                        api.updateStudent(
                            request.id,
                            StudentRequest(
                                request.rollNo,
                                request.fullName,
                                request.classId,
                                request.email,
                                request.phone,
                            ),
                        )
                    }
                    PendingActionType.DELETE_STUDENT -> {
                        val request = deleteAdapter.fromJson(action.payload) ?: continue
                        api.deleteStudent(request.id)
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
