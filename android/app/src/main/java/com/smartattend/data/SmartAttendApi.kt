package com.smartattend.data

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SmartAttendApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("api/dashboard")
    suspend fun dashboard(): DashboardResponse

    @GET("api/classes")
    suspend fun classes(): List<ClassResponse>

    @POST("api/classes")
    suspend fun createClass(@Body request: ClassRequest): ClassResponse

    @PUT("api/classes/{id}")
    suspend fun updateClass(@Path("id") id: Long, @Body request: ClassRequest): ClassResponse

    @DELETE("api/classes/{id}")
    suspend fun deleteClass(@Path("id") id: Long)

    @GET("api/students")
    suspend fun students(@Query("classId") classId: Long? = null): List<StudentResponse>

    @POST("api/students")
    suspend fun createStudent(@Body request: StudentRequest): StudentResponse

    @PUT("api/students/{id}")
    suspend fun updateStudent(@Path("id") id: Long, @Body request: StudentRequest): StudentResponse

    @DELETE("api/students/{id}")
    suspend fun deleteStudent(@Path("id") id: Long)

    @GET("api/attendance")
    suspend fun attendance(
        @Query("classId") classId: Long,
        @Query("date") date: String,
    ): List<AttendanceResponse>

    @POST("api/attendance")
    suspend fun saveAttendance(@Body request: AttendanceRequest): List<AttendanceResponse>

    @DELETE("api/attendance")
    suspend fun clearAttendance(
        @Query("classId") classId: Long,
        @Query("date") date: String,
    )

    @GET("api/reports/student")
    suspend fun studentReports(
        @Query("classId") classId: Long? = null,
        @Query("studentId") studentId: Long? = null,
    ): List<StudentReport>

    @GET("api/reports/date")
    suspend fun dateReports(
        @Query("classId") classId: Long,
        @Query("date") date: String,
    ): List<DateReport>
}
