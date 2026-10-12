package com.example.oportunia.data.remote.api

import com.example.oportunia.data.remote.dto.InternshipDto
import com.example.oportunia.data.remote.dto.InterviewDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface InterviewService {

    /**
     * Retrieves a list of all interviews.
     * This typically returns interviews that have been generated or scheduled.
     *
     * @return A list of internships that are associated with interviews.
     */
    @GET("interviews")
    suspend fun getAllInterviews(): Response<List<InternshipDto>>

    /**
     * Retrieves the details of a specific interview by its ID.
     *
     * @param id The unique identifier of the interview.
     * @return A single InterviewDto object containing interview details.
     */
    @GET("interview/{id}")
    suspend fun getInterviewById(@Path("id") id: Long): Response<InterviewDto>

    /**
     * Generates a simulated interview for a specific internship.
     * This is typically used when the student requests to generate an interview
     * based on a particular internship they applied to.
     *
     * @param internshipId The ID of the internship to generate the interview for.
     * @return The generated InterviewDto object.
     */
    @GET("interviews/{internshipId}")
    suspend fun generateInterview(@Path("internshipId") internshipId: Long): Response<InterviewDto>
}
