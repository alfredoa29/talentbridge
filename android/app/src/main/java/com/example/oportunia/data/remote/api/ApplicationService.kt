package com.example.oportunia.data.remote.api

import com.example.oportunia.data.remote.dto.ApplicationDto
import com.example.oportunia.data.remote.dto.CreateApplicationDto
import com.example.oportunia.data.remote.dto.StudentInternshipDto
import com.example.oportunia.data.remote.dto.ApplicationUpdateDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApplicationService {

    // Endpoint para obtener las aplicaciones de un estudiante
    @GET("applications/student/{studentId}")
    suspend fun getApplicationsByStudent(@Path("studentId") studentId: Long): Response<List<ApplicationDto>>

    // Endpoint para crear una nueva aplicación
    @POST("applications")
    suspend fun createApplication(
        @Body createApplicationDto: CreateApplicationDto
    ): Response<ApplicationDto>

    // Endpoint para actualizar el estado de una aplicación
    //@PATCH("applications/{applicationId}/status")
    @PUT("applications")
    suspend fun updateApplicationStatus(
        @Body applicationStatus: ApplicationUpdateDto
    ): Response<ApplicationUpdateDto>

    @GET("applications/{companyId}/students-internships")
    suspend fun getStudentsAndInternshipsByCompany(@Path("companyId") companyId: Long): Response<List<StudentInternshipDto>>

    @GET("applications/{applicationId}")
    suspend fun getApplicationById(@Path("applicationId") applicationId: Long): Response<ApplicationDto>



}