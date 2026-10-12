package com.example.oportunia.data.remote.api

import com.example.oportunia.data.remote.dto.AreaOfInterestDto
import com.example.oportunia.data.remote.dto.InternshipInputDto
import com.example.oportunia.data.remote.dto.InternshipDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface InternshipService {

    @GET("internships")
    suspend fun  getAllInternships(): Response<List<InternshipDto>>

    @GET("internships/{id}")
    suspend fun getInternshipById(@Path("id") id: Long): Response<InternshipDto>

    @GET("internships/student/{studentId}")
    suspend fun  getByFilteredInternships(@Path("studentId") id: Long): Response<List<InternshipDto>>

    @GET("internships/company/{compnayId}")
    suspend fun getAllInternshipsByCompanyId(@Path("compnayId") id: Long): Response<List<InternshipDto>>

    @POST("internships")
    suspend fun createInternship(@Body internship: InternshipInputDto): Response<InternshipDto>

    @PUT("internships")
    suspend fun updateInternship(@Body internshipInputDto:InternshipInputDto): Response<InternshipDto>

    @GET("internships/categories")
    suspend fun getAllCategories():Response<List<AreaOfInterestDto>>

}

