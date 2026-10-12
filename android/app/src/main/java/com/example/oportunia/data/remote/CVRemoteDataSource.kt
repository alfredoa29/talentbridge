package com.example.oportunia.data.remote

import android.content.ContentResolver
import android.net.Uri
import com.example.oportunia.data.remote.api.CVService
import com.example.oportunia.data.remote.dto.AnalizeCurriculumAIDto
import com.example.oportunia.domain.model.AnalizeCurriculumAI
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import javax.inject.Inject

class CVRemoteDataSource @Inject constructor(
    private val cVService: CVService
){
    suspend fun uploadCV(file: Uri, contentResolver: ContentResolver): Result<AnalizeCurriculumAIDto> = safeApiCall {
        val inputStream = contentResolver.openInputStream(file)
            ?: return@safeApiCall Response.error(400, "Archivo no encontrado".toResponseBody(null))

        val requestBody = inputStream.readBytes().toRequestBody("application/pdf".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(
            "file", "cv.pdf", requestBody
        )

        cVService.uploadCV(filePart)
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