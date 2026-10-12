package com.example.oportunia.domain.model

/* esta clase es para enviar la solicitud de pasantia al backend*/

data class CreateApplication(
    val internshipId: Long,
    val studentId: Long,
    val statusId: Int // representa el ID del estado (por ejemplo: 1 = PENDING)
)
