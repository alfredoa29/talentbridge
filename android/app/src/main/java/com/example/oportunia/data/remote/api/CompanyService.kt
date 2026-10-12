package com.example.oportunia.data.remote.api

import com.example.oportunia.data.remote.dto.CompanyDto
import com.example.oportunia.data.remote.dto.CompanyInput
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

interface CompanyService {

    @GET("companies/{id}")
    suspend fun getCompanyById(@Path("id") id: Long): Response<CompanyDto>

    @POST("companies/register")
    suspend fun register(@Body companyInput: CompanyInput): Response<CompanyDto>

    @Multipart
    @PUT("companies")
    suspend fun updateCompany(
        @Part("company") company: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<CompanyDto>

}