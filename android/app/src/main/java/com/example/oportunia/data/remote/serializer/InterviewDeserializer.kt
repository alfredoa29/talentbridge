package com.example.oportunia.data.remote.serializer

import com.example.oportunia.data.remote.dto.ExerciseSectionDto
import com.example.oportunia.data.remote.dto.InterviewDto
import com.example.oportunia.data.remote.dto.QuestionSectionDto
import com.example.oportunia.data.remote.dto.StudentDto
import com.google.gson.*
import java.lang.reflect.Type
/**
 * Custom JSON deserializer for [InterviewDto] objects.
 *
 * Handles special cases like:
 * - Mixed id formats (string or numeric)
 * - Date parsing
 * - Properly deserializing nested objects
 */
class InterviewDeserializer : JsonDeserializer<InterviewDto> {

    /**
     * Deserializa los datos JSON en un objeto [InterviewDto].
     *
     * @param json El elemento JSON a deserializar
     * @param typeOfT El tipo del objeto a deserializar
     * @param context El contexto de deserialización
     * @return Un objeto [InterviewDto] completamente poblado
     * @throws JsonParseException si ocurre un error durante el análisis
     */
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): InterviewDto {
        val jsonObject = json.asJsonObject

        val introduction = context.deserialize<QuestionSectionDto>(
            jsonObject.get("introduction"), QuestionSectionDto::class.java
        ) ?: QuestionSectionDto(questions = emptyList())

        val knowledgeAssessment = context.deserialize<QuestionSectionDto>(
            jsonObject.get("knowledgeAssessment"), QuestionSectionDto::class.java
        ) ?: QuestionSectionDto(questions = emptyList())

        val problemSolving = context.deserialize<ExerciseSectionDto>(
            jsonObject.get("problemSolving"), ExerciseSectionDto::class.java
        ) ?: ExerciseSectionDto(exercises = emptyList())

        val expectationsAndClosure = context.deserialize<QuestionSectionDto>(
            jsonObject.get("expectationsAndClosure"), QuestionSectionDto::class.java
        ) ?: QuestionSectionDto(questions = emptyList())

        return InterviewDto(
            introduction = introduction,
            knowledgeAssessment = knowledgeAssessment,
            problemSolving = problemSolving,
            expectationsAndClosure = expectationsAndClosure
        )
    }

}
