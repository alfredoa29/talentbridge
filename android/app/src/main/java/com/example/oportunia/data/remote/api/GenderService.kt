package com.example.oportunia.data.remote.api

import com.example.oportunia.data.remote.dto.CountryDto
import com.example.oportunia.data.remote.dto.GenderDto
import retrofit2.Response
import retrofit2.http.GET

interface GenderService {

    @GET("genders")
    suspend fun getAllGenders(): Response<List<GenderDto>>
}