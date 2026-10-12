package com.example.oportunia.data.remote.dto

data class AuthResponseDto(
    val token: String,
    val username: String, //este es username porque asi viene desde el back
    val authorities: List<AuthorityDto>
)