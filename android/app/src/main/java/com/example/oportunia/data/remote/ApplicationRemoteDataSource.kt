package com.example.oportunia.data.remote

import com.example.oportunia.data.remote.api.ApplicationService
import com.example.oportunia.data.remote.dto.ApplicationDto
import com.example.oportunia.data.remote.dto.CreateApplicationDto
import com.example.oportunia.data.remote.dto.StudentInternshipDto
import com.example.oportunia.data.remote.dto.ApplicationUpdateDto
import retrofit2.Response
import javax.inject.Inject

class ApplicationRemoteDataSource  @Inject constructor(
    private val applicationService: ApplicationService
){
    suspend fun getApplicationsByStudent(id: Long): Result<List<ApplicationDto>> = safeApiCall {
        applicationService.getApplicationsByStudent(id)
    }

    suspend fun createApplication (application: CreateApplicationDto): Result<ApplicationDto> = safeApiCall {
        applicationService.createApplication(application)
    }

    suspend fun getApplicationById(id: Long): Result<ApplicationDto> = safeApiCall {
        applicationService.getApplicationById(id)
    }

    suspend fun getStudentsAndInternshipsByCompany(companyId: Long): Result <List<StudentInternshipDto>> = safeApiCall {
        applicationService.getStudentsAndInternshipsByCompany(companyId)
    }

    suspend fun updateApplicationStatus(applicationUpdateDto: ApplicationUpdateDto): Result<ApplicationUpdateDto>  = safeApiCall {
         applicationService.updateApplicationStatus(applicationUpdateDto)
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