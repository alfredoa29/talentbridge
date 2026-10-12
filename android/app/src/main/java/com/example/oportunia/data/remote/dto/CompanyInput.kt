package com.example.oportunia.data.remote.dto

data class CompanyInput (
    val email: String,
    val password: String,
    val name: String? = "Añadir nombre",
    val website: String? = "Añadir link de la página web",
    val sectorListIds: List<Long> = emptyList()
)