package com.example.oportunia.data.repository

import com.example.oportunia.data.mapper.AreaOfInterestMapper
import com.example.oportunia.data.remote.AreaOfInterestRemoteDataSource
import com.example.oportunia.data.remote.SkillRemoteDataSource
import com.example.oportunia.data.remote.api.AreaOfInterestService
import com.example.oportunia.data.remote.dto.AreaOfInterestDto
import com.example.oportunia.domain.model.AreaOfInterest
import com.example.oportunia.domain.repository.AreaOfInterestRepository
import retrofit2.Response
import java.net.UnknownHostException
import javax.inject.Inject

class AreaOfInterestRepositoryImpl  @Inject constructor(
    private val remoteDataSource: AreaOfInterestRemoteDataSource,
    private val areaOfInterestMapper: AreaOfInterestMapper
): AreaOfInterestRepository {

    override suspend fun getAllAreaOfInteres(): Result<List<AreaOfInterest>> {
        return try {
            remoteDataSource.getAllAreasOfInterest().map { areasDtos ->
                areaOfInterestMapper.mapToDomainList(areasDtos)
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Network error: Cannot connect to server. Please check your internet connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching countries: ${e.message}"))
        }
    }


}