package com.example.oportunia.domain.repository

import com.example.oportunia.data.remote.dto.UserDto
import com.example.oportunia.domain.model.User

interface UserRepository {
    suspend fun loginUser(email: String, password: String): Result<UserDto>

    suspend fun findUserByEmail(email: String): Result<User>

    suspend fun findUserByUsername(username: String): Result<User>

    suspend fun create(email: String, password: String): Result<UserDto>
}