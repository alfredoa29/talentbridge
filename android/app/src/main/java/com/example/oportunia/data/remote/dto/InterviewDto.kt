package com.example.oportunia.data.remote.dto

/*
data class InterviewDto(
    val id: Long,
    val content: String,
    val createdAt: Long,
    val internship: InternshipDto //creo que esto era para poder sacar el id del internship a la que pertenece la interview en su momento
)
*/

data class  InterviewDto(
    val introduction: QuestionSectionDto,
    val knowledgeAssessment: QuestionSectionDto,
    val problemSolving: ExerciseSectionDto,
    val expectationsAndClosure: QuestionSectionDto

)

data class QuestionSectionDto(
    val questions: List<String> = emptyList()
)

data class ExerciseSectionDto(
    val exercises: List<String> = emptyList()
)