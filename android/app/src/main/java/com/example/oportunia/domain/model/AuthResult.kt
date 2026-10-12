package com.example.oportunia.domain.model

data class AuthResult (
    val token: String,
    val email: String,
    val authorities: List<Authority>
)

data class Authority(
    val authority: String
)