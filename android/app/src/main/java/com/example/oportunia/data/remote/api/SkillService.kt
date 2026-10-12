package com.example.oportunia.data.remote.api

import com.example.oportunia.data.remote.dto.CountryDto
import com.example.oportunia.data.remote.dto.SkillDto
import retrofit2.Response
import retrofit2.http.GET

interface SkillService {

    @GET("skills")
    suspend fun getAllSkills(): Response<List<SkillDto>>

}