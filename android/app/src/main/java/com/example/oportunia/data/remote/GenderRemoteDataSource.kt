package com.example.oportunia.data.remote

import com.example.oportunia.data.remote.api.GenderService
import com.example.oportunia.data.remote.dto.GenderDto
import retrofit2.Response
import javax.inject.Inject

class GenderRemoteDataSource @Inject constructor (
    private val genderService: GenderService
){

    suspend fun getAllGenders(): Result<List<GenderDto>> = safeApiCall {
        genderService.getAllGenders()
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