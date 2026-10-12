package com.example.oportunia.data.mapper

import android.util.Log
import com.example.oportunia.data.remote.dto.ExerciseSectionDto
import com.example.oportunia.data.remote.dto.InterviewDto
import com.example.oportunia.data.remote.dto.QuestionSectionDto
import com.example.oportunia.domain.model.ExerciseSection
import com.example.oportunia.domain.model.Interview
import com.example.oportunia.domain.model.QuestionSection
import javax.inject.Inject

class InterviewMapper @Inject constructor(
    private val internshipMapper: InternshipMapper  // Necesitamos el InternshipMapper para mapear Internship a InternshipDto y viceversa
) {

    // Mapea un InterviewDto a un Interview (dominio)
    fun mapToDomain(interviewDto: InterviewDto): Interview {
        return Interview(
            introduction = mapToDomain(interviewDto.introduction),
            knowledgeAssessment = mapToDomain(interviewDto.knowledgeAssessment),
            problemSolving = mapToDomain(interviewDto.problemSolving),
            expectationsAndClosure = mapToDomain(interviewDto.expectationsAndClosure)
        )
    }

    // Mapea una lista de InterviewDto a una lista de Interview (dominio)
    fun mapToDomainList(interviewsDto: List<InterviewDto>): List<Interview> {
        return interviewsDto.map { mapToDomain(it) }
    }

    // Mapea un Interview (dominio) a un InterviewDto (DTO)
    fun mapToDto(interview: Interview): InterviewDto {
        return InterviewDto(
            introduction = mapToDto(interview.introduction),
            knowledgeAssessment = mapToDto(interview.knowledgeAssessment),
            problemSolving = mapToDto(interview.problemSolving),
            expectationsAndClosure = mapToDto(interview.expectationsAndClosure)
        )
    }


    // Mapea una lista de Interview (dominio) a una lista de InterviewDto (DTO)
    fun mapToDtoList(interviews: List<Interview>): List<InterviewDto> {
        return interviews.map { mapToDto(it) }
    }

    private fun mapToDomain(sectionDto: QuestionSectionDto?): QuestionSection {
        Log.d("InterviewMapper", "Mapping QuestionSectionDto: $sectionDto")
        // Comprobar si sectionDto es nulo y devolver un objeto de dominio vacío en ese caso
        return QuestionSection(questions = sectionDto?.questions ?: emptyList())
    }
    private fun mapToDomain(sectionDto: ExerciseSectionDto?): ExerciseSection {
        Log.d("InterviewMapper", "Mapping ExerciseSectionDto: $sectionDto")
        return ExerciseSection(exercises = sectionDto?.exercises ?: emptyList())
    }

    private fun mapToDto(section: QuestionSection): QuestionSectionDto {
        return QuestionSectionDto(questions = section.questions)
    }

    private fun mapToDto(section: ExerciseSection): ExerciseSectionDto {
        return ExerciseSectionDto(exercises = section.exercises)
    }

}
