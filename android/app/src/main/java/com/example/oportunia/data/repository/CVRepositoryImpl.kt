package com.example.oportunia.data.repository

import android.net.Uri
import com.example.oportunia.data.mapper.ApplicationMapper
import com.example.oportunia.data.mapper.CVMapper
import com.example.oportunia.data.remote.ApplicationRemoteDataSource
import com.example.oportunia.data.remote.CVRemoteDataSource
import com.example.oportunia.domain.model.AnalizeCurriculumAI
import com.example.oportunia.domain.repository.CVRepository
import javax.inject.Inject
import android.content.ContentResolver

class CVRepositoryImpl@Inject constructor(
    private val remoteDataSource: CVRemoteDataSource,
    private val  cvMapper: CVMapper
): CVRepository {

    override suspend fun uploadCV(file: Uri, contentResolver: ContentResolver): Result<AnalizeCurriculumAI> {
        return try {
            val dtoResult = remoteDataSource.uploadCV(file, contentResolver)

            dtoResult.map { dto ->
                cvMapper.mapToDomain(dto)
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error uploading CV: ${e.message}", e))
        }
    }

}
