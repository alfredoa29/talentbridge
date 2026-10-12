package com.example.oportunia.domain.repository

import com.example.oportunia.domain.model.Sector

interface SectorRepository {
    suspend fun getAllSectors(): Result<List<Sector>>
}