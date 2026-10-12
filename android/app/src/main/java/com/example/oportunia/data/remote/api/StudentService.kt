package com.example.oportunia.data.remote.api

import com.example.oportunia.data.remote.dto.InternshipDto
import com.example.oportunia.data.remote.dto.StudentDto
import com.example.oportunia.data.remote.dto.StudentInput
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface StudentService {

    @GET("students")
    suspend fun getAllStudents(): Response<List<StudentDto>>

    @POST("students/register")
    suspend fun registerStudent(@Body studentInput: StudentInput): Response<StudentDto>

    @GET("students/{id}")
    suspend fun getStudentById(@Path("id") id: Long): Response<StudentDto>

    @GET("internshipsByStudent/{id}")
    suspend fun getInternshipsByStudent(@Path("id") id: Long): Response<List<InternshipDto>>

    @Multipart
    @PUT("students")
    suspend fun updateStudent(
        @Part("student") student: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<StudentDto>


}