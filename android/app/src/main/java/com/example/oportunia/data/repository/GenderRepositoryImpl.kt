package com.example.oportunia.data.repository

import com.example.oportunia.data.mapper.GenderMapper
import com.example.oportunia.data.remote.GenderRemoteDataSource
import com.example.oportunia.domain.model.Gender
import com.example.oportunia.domain.repository.GenderRepository
import java.net.UnknownHostException
import javax.inject.Inject

class GenderRepositoryImpl @Inject constructor(
    private  val remoteDataSource: GenderRemoteDataSource,
    private val genderMapper: GenderMapper
): GenderRepository {
    override suspend fun getAllGenders(): Result<List<Gender>> {
        return try {
            remoteDataSource.getAllGenders().map { genderDtos ->
                genderMapper.mapToDomainList(genderDtos)
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Network error: Cannot connect to server. Please check your internet connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching tasks: ${e.message}"))
        }
    }

}