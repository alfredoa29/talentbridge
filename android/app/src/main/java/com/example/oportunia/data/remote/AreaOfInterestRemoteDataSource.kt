package com.example.oportunia.data.remote

import com.example.oportunia.data.remote.api.AreaOfInterestService
import com.example.oportunia.data.remote.dto.AreaOfInterestDto
import com.example.oportunia.data.remote.dto.CountryDto
import retrofit2.Response
import javax.inject.Inject

class AreaOfInterestRemoteDataSource @Inject constructor(

    private val areaOfInterestService: AreaOfInterestService
){


    suspend fun getAllAreasOfInterest(): Result<List<AreaOfInterestDto>> = safeApiCall {
        areaOfInterestService.getAllAreaOfInterest()
    }



    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> = try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Response body was null"))
        } else {
            val errorBody = response.errorBody()?.string()
            Result.failure(Exception("API error ${response.code()}: $errorBody"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

}