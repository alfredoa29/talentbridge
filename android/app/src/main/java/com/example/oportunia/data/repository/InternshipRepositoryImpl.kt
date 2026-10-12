package com.example.oportunia.data.repository

import com.example.oportunia.data.mapper.InternshipMapper
import com.example.oportunia.data.remote.InternshipRemoteDataSource
import com.example.oportunia.data.remote.dto.AreaOfInterestDto
import com.example.oportunia.data.remote.dto.InternshipInputDto
import com.example.oportunia.domain.model.Internship
import com.example.oportunia.domain.repository.InternshipRepository
import java.net.UnknownHostException
import javax.inject.Inject

class InternshipRepositoryImpl @Inject constructor(
    private val remoteDataSource: InternshipRemoteDataSource,
    private val internshipMapper: InternshipMapper
): InternshipRepository {


    override suspend fun findAllInternships(): Result<List<Internship>> {
        return try {
            remoteDataSource.getAllInternships().map { internshipDtos ->
                internshipMapper.mapToDomainList(internshipDtos)
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Network error: Cannot connect to server. Please check your internet connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching tasks: ${e.message}"))
        }
    }

    override suspend fun findAllInternshipsByCompanyId(companyId: Long): Result<List<Internship>> {
        return try {
            remoteDataSource.getAllInternshipsByCompanyId(companyId).map { internshipDtos ->
                internshipMapper.mapToDomainList(internshipDtos)
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Network error: Cannot connect to server. Please check your internet connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching tasks: ${e.message}"))
        }
    }

    override suspend fun findTaskById(internshipId: Long): Result<Internship> {
        return try {
            remoteDataSource.getInternshipById(internshipId).map { internshipDto ->
                internshipMapper.mapToDomain(internshipDto)

            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Network error: Cannot connect to server. Please check your internet connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching tasks: ${e.message}"))
        }
    }


    override suspend fun createInternship(internshipInputDto: InternshipInputDto): Result<Internship> =
        remoteDataSource.createInternship(internshipInputDto).map { internshipDto ->
            internshipMapper.mapToDomain(internshipDto)
    }

    override suspend fun updateInternship(internshipInputDto: InternshipInputDto): Result<Internship> =
        remoteDataSource.updateInternship(internshipInputDto).map { internshipDto ->
            internshipMapper.mapToDomain(internshipDto)

    }

    override suspend fun findAllInternshipsByStudentId(studentId: Long): Result<List<Internship>> {

        return try {
            remoteDataSource.getAllInternshipsByStudentId(studentId).map { internshipDtos ->
                internshipMapper.mapToDomainList(internshipDtos)
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Network error: Cannot connect to server. Please check your internet connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching internships by student : ${e.message}"))
        }
    }

    override suspend fun getAllCategories(): Result<List<AreaOfInterestDto>> =
        remoteDataSource.getAllCategories()

}