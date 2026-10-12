package com.example.oportunia.data.remote.dto

data class AnalizeCurriculumAIDto(
    val strengths: List<String>,
    val weaknesses: List<String>,
    val improvementAreas : List<String>,
    val overallAssessment : String
)
