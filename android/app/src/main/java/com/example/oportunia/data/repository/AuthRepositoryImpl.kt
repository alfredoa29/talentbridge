package com.example.oportunia.data.repository

import android.util.Log
import com.example.oportunia.data.local.AuthPreferences
import com.example.oportunia.data.remote.AuthRemoteDataSource

import com.example.oportunia.domain.model.Credentials
import com.example.oportunia.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authPreferences: AuthPreferences
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            Log.d("AuthRepositoryImpl", "Login attempt for user: $username")
            val credentials = Credentials(username, password)

            authRemoteDataSource.login(credentials)
                .onSuccess { authResult ->
                    val token = authResult.token
                    val role = authResult.authorities.firstOrNull()?.authority ?: ""
                    if (token.isNotBlank()) {
                        Log.d(
                            "AuthRepositoryImpl",
                            "Login successful, token: ${token.take(10)}..."
                        )
                        // Save auth state to preferences
                        authPreferences.saveAuthToken(token)
                        authPreferences.saveUsername(username)
                        authPreferences.saveUserRole(role)
                    } else {
                        Log.e("AuthRepositoryImpl", "Received empty token in auth result")
                    }
                }
                .onFailure { error ->
                    Log.e("AuthRepositoryImpl", "Login failed: ${error.message}")
                }
                .map { }
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Login exception: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            authRemoteDataSource.logout()
            authPreferences.clearAuthData()
            Result.success(Unit)
        }  catch (e: Exception) {
            Result.failure<Unit>(e)
        }
    }

    override suspend fun isAuthenticated(): Result<Boolean> {
        return try {
            val token = authPreferences.getAuthToken()
            val isAuthenticated = !token.isNullOrEmpty()
            Log.d("AuthRepositoryImpl", "Authentication check: $isAuthenticated")
            Result.success(isAuthenticated)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Auth check exception: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Result<String?> {
        return try {
            val username = authPreferences.getUsername()
            Log.d("AuthRepositoryImpl", "Current user: $username")
            Result.success(username)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Get current user exception: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getUsername(): Result<String?> {
        return try {
            val username = authPreferences.getUsername()

            if (username == null ){
                Log.d("AuthRepositoryImpl", "Username check: $username")
            }

            Result.success(username)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Auth check exception: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getUserRole(): Result<String?> {
        return try {
            val userRole = authPreferences.getUserRole()

            if (userRole == null ){
                Log.d("AuthRepositoryImpl", "userRole check: $userRole")
            }

            Result.success(userRole)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Auth check exception: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getAuthToken(): Result<String?> {
        return try {
            val authToken = authPreferences.getAuthToken()

            if (authToken == null ){
                Log.d("AuthRepositoryImpl", "AuthToken check: $authToken")
            }

            Result.success(authToken)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Auth check exception: ${e.message}", e)
            Result.failure(e)
        }
    }

}