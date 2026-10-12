package com.example.oportunia.data.remote

import retrofit2.Response
import com.example.oportunia.data.remote.api.CompanyService
import com.example.oportunia.data.remote.dto.CompanyDto
import com.example.oportunia.data.remote.dto.CompanyInput
import com.example.oportunia.data.remote.dto.CompanyUpdateDto
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class CompanyRemoteDataSource @Inject constructor(
    private val companyService: CompanyService,

    ){

    suspend fun getCompanyById(id: Long): Result<CompanyDto> = safeApiCall {
        companyService.getCompanyById(id)
    }

    suspend fun register(companyInputDto: CompanyInput): Result<CompanyDto> = safeApiCall {
        companyService.register(companyInputDto)
    }

    suspend fun updateCompany(company: CompanyUpdateDto, profileImage: File? = null): Result<CompanyDto> = safeApiCall {
        val gson = Gson()
        val companyJson = gson.toJson(company)
        val companyBody = companyJson.toRequestBody("application/json".toMediaTypeOrNull())

        val imagePart = profileImage?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", it.name, requestFile)
        }

        companyService.updateCompany(companyBody, imagePart)

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