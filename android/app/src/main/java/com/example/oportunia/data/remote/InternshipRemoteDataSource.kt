package com.example.oportunia.data.remote

import com.example.oportunia.data.remote.dto.InternshipDto
import com.example.oportunia.data.remote.api.InternshipService
import com.example.oportunia.data.remote.dto.AreaOfInterestDto
import com.example.oportunia.data.remote.dto.InternshipInputDto
import retrofit2.Response
import javax.inject.Inject

class InternshipRemoteDataSource @Inject constructor(
    private val internshipService: InternshipService
) {

    suspend fun getAllInternships(): Result<List<InternshipDto>> = safeApiCall {
        internshipService.getAllInternships()
    }

    suspend fun getInternshipById(id : Long): Result<InternshipDto> = safeApiCall {
        internshipService.getInternshipById(id)
    }

    suspend fun getAllInternshipsByStudentId(studentId: Long): Result<List<InternshipDto>> = safeApiCall {
        internshipService.getByFilteredInternships(studentId)
    }

    suspend fun getAllInternshipsByCompanyId(companyId: Long): Result<List<InternshipDto>> = safeApiCall {
        internshipService.getAllInternshipsByCompanyId(companyId)
    }

    suspend fun createInternship(internshipInputDto: InternshipInputDto): Result<InternshipDto> = safeApiCall {
        internshipService.createInternship(internshipInputDto)
    }

    suspend fun getAllCategories(): Result<List<AreaOfInterestDto>> = safeApiCall {
        internshipService.getAllCategories()
    }

    suspend fun updateInternship(internshipInputDto: InternshipInputDto): Result<InternshipDto> = safeApiCall {
        internshipService.updateInternship(internshipInputDto)
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