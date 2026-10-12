package com.example.oportunia.domain.model

data class AnalizeCurriculumAI(
    val strengths: List<String>,
    val weaknesses: List<String>,
    val improvementAreas : List<String>,
    val overallAssessment : String
)
