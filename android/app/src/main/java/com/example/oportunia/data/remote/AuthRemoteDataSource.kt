package com.example.oportunia.data.remote

import android.util.Log
import com.example.oportunia.data.mapper.AuthMapper
import com.example.oportunia.data.remote.api.AuthService
import com.example.oportunia.domain.model.AuthResult
import com.example.oportunia.domain.model.Authority
import com.example.oportunia.domain.model.Credentials
import retrofit2.Response
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRemoteDataSource @Inject constructor(
    private val authService: AuthService
) {

    suspend fun login(credentials: Credentials): Result<AuthResult> = withContext(Dispatchers.IO) {
        try {
            // Convert credentials and log the request payload for debugging
            val requestDto = AuthMapper.credentialsToDto(credentials)
            Log.d(
                "AuthRemoteDataSource",
                "Login request payload: username=${requestDto.email}, password length=${requestDto.password.length}"
            )

            val response = authService.login(requestDto)

            if (response.isSuccessful) {
                // Extract the token from the Authorization header
                val authHeader = response.headers()["Authorization"]

                val body = response.body()
                if (!authHeader.isNullOrBlank() && body != null) {
                    val authorities = body.authorities.map { Authority(it.authority) } // ← Aquí haces la conversión
                    return@withContext Result.success(
                        AuthResult(
                            token = authHeader,
                            email = body.username,
                            authorities = authorities
                        )
                    )
                }


                // Fallback to response body if header is not present
                //val body = response.body()
                return@withContext if (body != null) {
                    Log.d("AuthRemoteDataSource", "Login successful with response body: $body")
                    Result.success(AuthMapper.dtoToAuthResult(body))
                } else {
                    Log.e(
                        "AuthRemoteDataSource",
                        "Login response body was null and no Authorization header found"
                    )
                    Result.failure(Exception("No token found in response"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                // Specific error handling based on HTTP status code
                val errorMessage = when (response.code()) {
                    403 -> "Invalid username or password"
                    401 -> "Unauthorized access"
                    404 -> "Service not found"
                    500 -> "Server error occurred"
                    else -> "API error ${response.code()}: $errorBody"
                }

                Log.e(
                    "AuthRemoteDataSource",
                    "Login failed: $errorMessage"
                )
                return@withContext Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("AuthRemoteDataSource", "Login exception: ${e.message}", e)
            return@withContext Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> = safeApiCall {
        authService.logout()
    }

    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> = try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Response body was null"))
        } else {
            val errorBody = response.errorBody()?.string()
            // Improved error handling
            val errorMessage = when (response.code()) {
                403 -> "Invalid username or password"
                401 -> "Unauthorized access"
                else -> "API error ${response.code()}: $errorBody"
            }
            Result.failure(Exception(errorMessage))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}