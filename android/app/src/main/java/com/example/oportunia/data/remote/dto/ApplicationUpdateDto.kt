package com.example.oportunia.data.remote.dto

data class ApplicationUpdateDto(
    val id: Long, //applicationId
    val statusId: Int // representa el ID del estado (por ejemplo: 1 = PENDING)


)