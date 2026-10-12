package com.example.oportunia.data.remote

import com.example.oportunia.data.remote.api.SkillService
import com.example.oportunia.data.remote.dto.CountryDto
import com.example.oportunia.data.remote.dto.SkillDto
import retrofit2.Response
import javax.inject.Inject

class SkillRemoteDataSource @Inject constructor(
    private val skillService: SkillService
){

    suspend fun getAllSkills(): Result<List<SkillDto>> = safeApiCall {
        skillService.getAllSkills()
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