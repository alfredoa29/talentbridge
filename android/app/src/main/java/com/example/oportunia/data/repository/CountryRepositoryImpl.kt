package com.example.oportunia.data.repository

import com.example.oportunia.data.mapper.CountryMapper
import com.example.oportunia.data.remote.CountryRemoteDataSource
import com.example.oportunia.domain.model.Country
import com.example.oportunia.domain.repository.CountryRepository
import java.net.UnknownHostException
import javax.inject.Inject

class CountryRepositoryImpl  @Inject constructor(
    private val remoteDataSource: CountryRemoteDataSource,
    private val countryMapper: CountryMapper
): CountryRepository {
    override suspend fun getAllCountries(): Result<List<Country>> {
        return try {
            remoteDataSource.getAllCountries().map { internshipDtos ->
                countryMapper.mapToDomainList(internshipDtos)
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Network error: Cannot connect to server. Please check your internet connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching countries: ${e.message}"))
        }
    }
    }


