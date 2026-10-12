package com.example.oportunia.data.remote.api

import com.example.oportunia.data.remote.dto.UserDto
import com.example.oportunia.domain.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface UserService {

    // Endpoint para obtener todos los usuarios
    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    // Endpoint para obtener un usuario específico de la lista de usuarios
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Long): Response<User>

    @GET("users/username/{username}")
    suspend fun getUserByUsername(@Path("username") username: String): Response<UserDto>

    @POST("users")
    suspend fun createUser(@Body userInput: User): Response<UserDto>
}