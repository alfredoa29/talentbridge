package com.example.oportunia.data.repository

import com.example.oportunia.data.mapper.ApplicationMapper
import com.example.oportunia.data.remote.ApplicationRemoteDataSource
import com.example.oportunia.domain.model.Application
import com.example.oportunia.domain.model.CreateApplication
import com.example.oportunia.domain.model.StudentInternship
import com.example.oportunia.domain.model.ApplicationStatusChange
import com.example.oportunia.domain.repository.ApplicationRepository
import java.net.UnknownHostException
import javax.inject.Inject

class ApplicationRepositoryImpl @Inject constructor(
    private val remoteDataSource: ApplicationRemoteDataSource,
    private val applicationMapper: ApplicationMapper
): ApplicationRepository {
    override suspend fun getApplicationsByStudent(studentId: Long): Result<List<Application>> {
        return try {
            remoteDataSource.getApplicationsByStudent(studentId).map { ApplicationDtos ->
                applicationMapper.mapToDomainList(ApplicationDtos)
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Network error: Cannot connect to server. Please check your internet connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching applications: ${e.message}"))
        }
    }

    override suspend fun createApplication(application: CreateApplication): Result<Application> {
        return try {
            /*
            val dto = applicationMapper.mapCreateApplicationToDto(application)
            val result = remoteDataSource.createApplication(dto)
            result.map { applicationMapper.mapToDomain(it) }


             */
            remoteDataSource.createApplication(applicationMapper.mapCreateApplicationToDto(application)).map { applicationDto ->
                applicationMapper.mapToDomain(applicationDto)
            }

        } catch (e: Exception) {
            Result.failure(Exception("Error fetching Application: ${e.message}"))
        }
    }

    override suspend fun getStudentsAndInternshipsByCompany(companyId: Long): Result<List<StudentInternship>> {
        return try {
            remoteDataSource.getStudentsAndInternshipsByCompany(companyId).map { studentInternshipDto ->
                applicationMapper.mapStudentInternshipToDomainList(studentInternshipDto)
            }


        }catch (e: Exception){
            Result.failure(Exception("Error fetching StudentInternship: ${e.message}"))
        }
    }

    override suspend fun getApplicationById(applicationId: Long): Result<Application> =
        remoteDataSource.getApplicationById(applicationId).map { applicationDto ->
            applicationMapper.mapToDomain(applicationDto)
        }

    override suspend fun updateApplicationState(applicationStatusChange: ApplicationStatusChange): Result<ApplicationStatusChange> =
       remoteDataSource.updateApplicationStatus(applicationMapper.mapToDto(applicationStatusChange)).map { updateApplicationDto ->
           applicationMapper.mapToDomain(updateApplicationDto)
       }
}







