package com.example.oportunia.data.mapper

import com.example.oportunia.data.remote.dto.AuthRequestDto
import com.example.oportunia.data.remote.dto.AuthResponseDto
import com.example.oportunia.domain.model.AuthResult
import com.example.oportunia.domain.model.Authority
import com.example.oportunia.domain.model.Credentials

object AuthMapper {

    fun credentialsToDto(credentials: Credentials): AuthRequestDto {
        return AuthRequestDto(
            email = credentials.username,
            password = credentials.password
        )
    }

    fun dtoToAuthResult(dto: AuthResponseDto): AuthResult {
        return AuthResult(
            token = dto.token,
            email = dto.username,
            authorities = dto.authorities.map { Authority(it.authority) }

        )
    }



}