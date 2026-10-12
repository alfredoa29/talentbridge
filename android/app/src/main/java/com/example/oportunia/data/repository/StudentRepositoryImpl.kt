package com.example.oportunia.data.repository

import com.example.oportunia.data.mapper.InternshipMapper
import com.example.oportunia.data.mapper.StudentMapper
import com.example.oportunia.data.remote.StudentRemoteDataSource
import com.example.oportunia.data.remote.dto.StudentInput
import com.example.oportunia.data.remote.dto.StudentUpdateDto
import com.example.oportunia.domain.model.Internship
import com.example.oportunia.domain.model.Student
import com.example.oportunia.domain.repository.StudentRepository
import java.io.File
import java.net.UnknownHostException
import javax.inject.Inject

class StudentRepositoryImpl @Inject constructor(
    private val remoteDataSource: StudentRemoteDataSource,
    private val studentMapper: StudentMapper,
    private val internshipMapper: InternshipMapper
) : StudentRepository {

    // Fetches all students and maps them to domain entities
    override suspend fun findAllStudents(): Result<List<Student>> {
        return try {
            remoteDataSource.getAllStudents().map { studentDtos ->
                studentMapper.mapToDomainList(studentDtos)
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Network error: Cannot connect to server. Please check your internet connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching students: ${e.message}"))
        }
    }

    override suspend fun register(studentToRegister: StudentInput): Result<Student> {
        return try {
            val response = remoteDataSource.register(studentToRegister)
            response.mapCatching { studentDto ->
                studentMapper.mapToDomain(studentDto)
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error registering the student", e))
        }
    }

    // Fetches a student by their ID and maps it to a domain entity
    override suspend fun findStudentById(studentId: Long): Result<Student> =
        remoteDataSource.getStudentById(studentId).map { studentDto ->
            studentMapper.mapToDomain(studentDto)
        }

    override suspend fun updateStudent(
        student: StudentUpdateDto,
        profileImage: File?
    ): Result<Student> =
        remoteDataSource.updateStudent(student, profileImage).map {  studentDto ->
            studentMapper.mapToDomain(studentDto)
        }


    override suspend fun getInternshipsByStudent(studentId: Long): Result<List<Internship>> {
        return try {
            // Fetch internships by student ID from the remote data source
            remoteDataSource.getInternshipsByStudent(studentId).map { internshipDtos ->
                // Map the list of InternshipDto to the domain Internship model
                internshipMapper.mapToDomainList(internshipDtos)
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Network error: Cannot connect to server. Please check your internet connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching internships for student: ${e.message}"))
        }
    }



}


