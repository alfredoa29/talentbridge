package com.example.oportunia.data.remote.api

import com.example.oportunia.data.remote.dto.AreaOfInterestDto
import com.example.oportunia.data.remote.dto.CountryDto
import com.example.oportunia.domain.model.AreaOfInterest
import retrofit2.Response
import retrofit2.http.GET

interface AreaOfInterestService {
    @GET("areasofinterest")
    suspend fun getAllAreaOfInterest(): Response<List<AreaOfInterestDto>>
}