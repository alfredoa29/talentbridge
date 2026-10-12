package com.example.oportunia.domain.model

data class Application(
    val id: Long,
    val internship: Internship,
    val student: Student,
    val status: ApplicationStatus
)


data class ApplicationStatus(
    val id: Long,
    val name: String
)