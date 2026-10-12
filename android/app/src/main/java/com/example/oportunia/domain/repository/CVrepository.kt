package com.example.oportunia.domain.repository

import android.content.ContentResolver
import android.net.Uri
import com.example.oportunia.domain.model.AnalizeCurriculumAI

interface CVRepository {
    suspend fun uploadCV(file: Uri, contentResolver: ContentResolver): Result<AnalizeCurriculumAI>
}
