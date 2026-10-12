package com.example.oportunia.data.remote.api

import com.example.oportunia.data.remote.dto.AuthRequestDto
import com.example.oportunia.data.remote.dto.AuthResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("users/login")
    suspend fun login(@Body request: AuthRequestDto): Response<AuthResponseDto>

    @POST("?")
    suspend fun logout(): Response<Unit>
}