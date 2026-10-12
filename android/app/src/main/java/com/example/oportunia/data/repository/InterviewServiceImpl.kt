package com.example.oportunia.data.repository

import com.example.oportunia.data.mapper.InternshipMapper
import com.example.oportunia.data.mapper.InterviewMapper
import com.example.oportunia.data.remote.InternshipRemoteDataSource
import com.example.oportunia.data.remote.InterviewRemoteDataSource
import com.example.oportunia.data.remote.api.InterviewService
import com.example.oportunia.data.remote.dto.InternshipDto
import com.example.oportunia.data.remote.dto.InterviewDto
import com.example.oportunia.domain.model.Interview
import com.example.oportunia.domain.repository.InterviewRepository
import retrofit2.Response
import javax.inject.Inject

class InterviewServiceImpl @Inject constructor(
    private val remoteDataSource: InterviewRemoteDataSource,
    private val intervieMapper: InterviewMapper
) : InterviewRepository {


    override suspend fun findAllInterviews(): Result<List<Interview>> {
        TODO("Not yet implemented")
    }

    override suspend fun findInterviewById(interviewId: Long): Result<Interview> {
        TODO("Not yet implemented")
    }

    override suspend fun generateInterview(internshipId: Long): Result<Interview> =
        remoteDataSource.generateInterview(internshipId).map {  interviewDto ->
            intervieMapper.mapToDomain(interviewDto)
        }

    }



