package com.example.oportunia.data.remote.dto

data class UserDto(
    val id: Long,
    val email: String,
    val enabled: Boolean,
    val tokenExpired: Boolean
)
