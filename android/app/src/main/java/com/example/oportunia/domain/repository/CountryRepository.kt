package com.example.oportunia.domain.repository

import com.example.oportunia.data.remote.dto.CountryDto
import com.example.oportunia.domain.model.Country

interface CountryRepository {
    suspend fun  getAllCountries(): Result<List<Country>>
}