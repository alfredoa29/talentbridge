package com.example.oportunia.domain.repository

import com.example.oportunia.data.remote.dto.StudentInput
import com.example.oportunia.data.remote.dto.StudentUpdateDto
import com.example.oportunia.domain.model.Internship
import com.example.oportunia.domain.model.Student
import java.io.File

interface StudentRepository {
    suspend fun findAllStudents(): Result<List<Student>>
    suspend fun register(studentToRegister: StudentInput): Result<Student>
    suspend fun findStudentById(studentId: Long): Result<Student>
    suspend fun updateStudent(student: StudentUpdateDto, profileImage: File? = null): Result<Student>
    suspend fun getInternshipsByStudent(studentId: Long): Result<List<Internship>>
}