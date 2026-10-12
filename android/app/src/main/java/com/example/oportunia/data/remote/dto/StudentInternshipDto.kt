package com.example.oportunia.data.remote.dto

data class StudentInternshipDto(
    val applicationId: Long,
    val studentId: Long,
    val studentName: String,
    var profilePicUrl: String,
    val internshipId: Long,
    val internshipTitle: String,
    val companyName: String,
)
