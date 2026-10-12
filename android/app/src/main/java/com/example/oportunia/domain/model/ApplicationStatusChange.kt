package com.example.oportunia.domain.model

data class ApplicationStatusChange(
    val applicationId: Long,
    val statusId: Int // representa el ID del estado (por ejemplo: 1 = PENDING)
)
