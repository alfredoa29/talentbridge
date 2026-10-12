package com.example.oportunia.data.remote.dto

data class ApplicationDto(
    val id: Long,
    val internship: InternshipDto,
    val student: StudentDto,
    val status: ApplicationStatusDto // "pending", "approved", "rejected"
)

data class ApplicationStatusDto(
    val id: Long,
    val name: String
)