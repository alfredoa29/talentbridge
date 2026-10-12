package com.example.oportunia.domain.model

data class Company(
    override val id: Long = 0,
    override val email: String = "",
    override val password: String = "",
    override val enabled: Boolean = true,
    override val tokenExpired: Boolean = false,
    val name: String,
    val website: String = "",
    val pictureUrl: String = "",
    var internships: List<Internship> = emptyList(),
    val sectorList: List<Sector>
) : User(id, email, password, enabled, tokenExpired)