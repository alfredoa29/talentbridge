package com.example.oportunia.domain.repository

interface AuthRepository {

    suspend fun login(username: String, password: String): Result<Unit>

    suspend fun logout(): Result<Unit>

    suspend fun isAuthenticated(): Result<Boolean>

    suspend fun getCurrentUser(): Result<String?>

    suspend fun getUsername(): Result<String?>
    suspend fun getUserRole(): Result<String?>
    suspend fun getAuthToken(): Result<String?>

}