package com.example.oportunia.data.remote

import com.example.oportunia.data.remote.api.InterviewService
import com.example.oportunia.data.remote.dto.InterviewDto
import retrofit2.Response
import javax.inject.Inject


class InterviewRemoteDataSource @Inject constructor(
    private val interviewService: InterviewService
) {

    suspend fun generateInterview(internshipId: Long):  Result<InterviewDto> = safeApiCall {
        interviewService.generateInterview(internshipId)
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


