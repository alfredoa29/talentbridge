package com.example.oportunia.data.repository

import com.example.oportunia.data.mapper.SectorMapper
import com.example.oportunia.data.remote.SectorRemoteDataSource
import com.example.oportunia.domain.model.Sector
import com.example.oportunia.domain.model.Skill
import com.example.oportunia.domain.repository.SectorRepository
import java.net.UnknownHostException
import javax.inject.Inject

class SectorRepositoryImpl @Inject constructor(
    private val remoteDataSource: SectorRemoteDataSource,
    private val sectorMapper: SectorMapper
) : SectorRepository {
    override suspend fun getAllSectors(): Result<List<Sector>> {

        return try {
            remoteDataSource.getAllSectors().map { sectorsDtos ->
                sectorMapper.mapToDomainList(sectorsDtos)
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Network error: Cannot connect to server. Please check your internet connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching sectors: ${e.message}"))
        }
    }
}