package com.example.oportunia.data.remote.dto

/*Este DTO solo lleva los datos necesarios para crear la aplicación.*/
data class CreateApplicationDto(
    val internshipId: Long,
    val studentId: Long,
    val statusId: Int
)
