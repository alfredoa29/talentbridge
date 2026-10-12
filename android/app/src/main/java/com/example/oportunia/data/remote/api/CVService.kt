package com.example.oportunia.data.remote.api

import com.example.oportunia.data.remote.dto.AnalizeCurriculumAIDto
import com.example.oportunia.domain.model.AnalizeCurriculumAI
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CVService {

    @Multipart
    @POST("curriculums/upload")
    suspend fun uploadCV(
        @Part file: MultipartBody.Part
    ): Response<AnalizeCurriculumAIDto>

}