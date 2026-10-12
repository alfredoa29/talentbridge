package com.example.oportunia.data.remote.dto

data class StudentUpdateDto(
    val id: Long,
    val fullName: String,
    val email: String,
    val university: String,
    val career: String,
    val username: String,
    val genderId: Long,
    val countryId: Long,
    val skillIds: List<Long>,
    val areaOfInterestIds: List<Long>
    )