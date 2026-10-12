package com.example.oportunia.data.remote.dto

data class StudentInput (
    val email: String,
    val password: String,
    val id: Int? = null,
    val fullName: String? = "Añadir nombre completo",
    val university: String? = "Añadir universidad",
    val career: String? = "Añadir carrera",
    val username: String? = "Añadir nombre de usuario",
    val countryId: Long = 1L,
    val genderId: Long = 1L,
    val skillIds: List<Long> = emptyList(),
    val areaOfInterestIds: List<Long> = emptyList()
)
