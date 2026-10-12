package com.example.oportunia.domain.repository

import com.example.oportunia.domain.model.Application
import com.example.oportunia.domain.model.CreateApplication
import com.example.oportunia.domain.model.StudentInternship
import com.example.oportunia.domain.model.ApplicationStatusChange

interface ApplicationRepository {
    suspend fun getApplicationsByStudent(studentId: Long): Result<List<Application>>
    suspend fun createApplication(application: CreateApplication): Result<Application>
    suspend fun getStudentsAndInternshipsByCompany(companyId: Long): Result<List<StudentInternship>>
    suspend fun  getApplicationById(applicationId: Long): Result<Application>
    suspend fun updateApplicationState(applicationStatusChange: ApplicationStatusChange): Result<ApplicationStatusChange>
}