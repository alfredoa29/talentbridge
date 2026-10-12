package com.example.oportunia.domain.model

import java.sql.Date

/*
/**
 * Representa los detalles de una entrevista generada para una pasantía.
 *
 * @param id Identificador único de la entrevista.
 * @param internship La pasantía relacionada con esta entrevista.
 * @param content El contenido generado de la entrevista (simulada).
 * @param createdAt Fecha de creación de la entrevista.
 */
data class Interview(
    val id: Long,                    // Identificador único de la entrevista
    val internship: Internship,      // Relaciona la entrevista con una pasantía específica (objeto completo)
    val content: String,             // Contenido generado de la entrevista (simulada)
    val createdAt: Long             // Fecha y hora en la que se generó la entrevista (timestamp)
)

 */

data class  Interview(
    val introduction: QuestionSection,
    val knowledgeAssessment: QuestionSection,
    val problemSolving: ExerciseSection,
    val expectationsAndClosure: QuestionSection

)

data class QuestionSection(
    val  questions: List<String>
)

data class ExerciseSection(
    val exercises: List<String>
)