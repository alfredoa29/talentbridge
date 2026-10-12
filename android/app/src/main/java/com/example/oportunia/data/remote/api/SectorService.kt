package com.example.oportunia.data.remote.api

import com.example.oportunia.data.remote.dto.SectorDto
import retrofit2.Response
import retrofit2.http.GET

interface SectorService {

    @GET("sectors")
    suspend fun getAllSectors(): Response<List<SectorDto>>
}