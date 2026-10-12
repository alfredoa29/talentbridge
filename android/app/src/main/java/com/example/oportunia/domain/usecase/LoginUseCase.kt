package com.example.oportunia.domain.usecase

import android.util.Log
import com.example.oportunia.data.repository.AuthRepositoryImpl
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepositoryImpl: AuthRepositoryImpl) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        if (email.isBlank()) {
            return Result.failure(Exception("Email cannot be empty"))
        }
        if (password.isBlank()) {
            return Result.failure(Exception("Password cannot be empty"))
        }
        return try {

            Log.d("LoginUseCase", "Attempting login with email: $email")
            Log.d("LoginUseCase", "Attempting login with password: $password")
            authRepositoryImpl.login(email, password)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure<Unit>(e)
        }
    }
}