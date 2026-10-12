package com.example.oportunia.domain.repository

import com.example.oportunia.domain.model.AreaOfInterest
import com.example.oportunia.domain.model.Country

interface AreaOfInterestRepository {
    suspend fun  getAllAreaOfInteres(): Result<List<AreaOfInterest>>
}