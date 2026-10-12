package com.example.oportunia.data.remote

import com.example.oportunia.data.remote.api.UserService
import com.example.oportunia.data.remote.dto.UserDto
import com.example.oportunia.domain.model.User
import retrofit2.Response
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userService: UserService
) {
    suspend fun getUsers(): Result<List<User>> = safeApiCall {
        userService.getUsers()
    }

    suspend fun getUserById(id: Long): Result<User> = safeApiCall {
        userService.getUserById(id)
    }

    suspend fun getUserByUsername(username: String): Result<UserDto> = safeApiCall {
        userService.getUserByUsername(username)
    }


    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> = try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Response body was null"))
        } else {
            val errorBody = response.errorBody()?.string()
            Result.failure(Exception("API error ${response.code()}: $errorBody"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
