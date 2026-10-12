package com.example.oportunia.domain.model

data class StudentInternship(
    val applicationId: Long,
    val studentId: Long,
    val studentName: String,
    var profilePicUrl: String,
    val internshipId: Long,
    val internshipTitle: String,
    val companyName: String,
)