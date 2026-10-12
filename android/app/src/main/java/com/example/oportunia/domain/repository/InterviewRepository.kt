package com.example.oportunia.domain.repository

import com.example.oportunia.domain.model.Interview

interface InterviewRepository {

    suspend fun findAllInterviews(): Result<List<Interview>>

    suspend fun findInterviewById(interviewId: Long): Result<Interview>
    suspend fun generateInterview(internshipId: Long): Result<Interview>

}