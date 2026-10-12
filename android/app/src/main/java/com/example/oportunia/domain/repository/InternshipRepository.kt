package com.example.oportunia.domain.repository

import com.example.oportunia.data.remote.dto.AreaOfInterestDto
import com.example.oportunia.data.remote.dto.InternshipInputDto
import com.example.oportunia.domain.model.Internship

interface InternshipRepository {

    suspend fun  findAllInternships(): Result<List<Internship>>

    suspend fun findAllInternshipsByCompanyId(companyId: Long): Result<List<Internship>>

    suspend fun findTaskById(internshipId: Long): Result<Internship>

    suspend fun createInternship(dto: InternshipInputDto): Result<Internship>

    suspend fun updateInternship(internshipInputDto: InternshipInputDto): Result<Internship>

    suspend fun findAllInternshipsByStudentId(studentId: Long): Result<List<Internship>>

    suspend fun getAllCategories(): Result<List<AreaOfInterestDto>>


}