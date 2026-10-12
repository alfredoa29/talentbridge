package com.example.oportunia.data.mapper


import com.example.oportunia.data.remote.dto.AnalizeCurriculumAIDto
import com.example.oportunia.domain.model.AnalizeCurriculumAI
import javax.inject.Inject

class CVMapper @Inject constructor() {

    fun mapToDomain(dto: AnalizeCurriculumAIDto): AnalizeCurriculumAI {
        return AnalizeCurriculumAI(
            strengths = dto.strengths,
            weaknesses = dto.weaknesses,
            improvementAreas = dto.improvementAreas,
            overallAssessment = dto.overallAssessment
        )
    }

    fun mapToDto(domain: AnalizeCurriculumAI): AnalizeCurriculumAIDto {
        return AnalizeCurriculumAIDto(
            strengths = domain.strengths,
            weaknesses = domain.weaknesses,
            improvementAreas = domain.improvementAreas,
            overallAssessment = domain.overallAssessment
        )
    }
}
