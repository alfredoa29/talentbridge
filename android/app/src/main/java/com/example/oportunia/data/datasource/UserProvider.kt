package com.example.oportunia.data.datasource

import com.example.oportunia.domain.model.User
import javax.inject.Inject

class UserProvider  @Inject constructor(){
    private val mockUser = User(
        id = 1,
        email = "correo@mail.com",
        password = "12345",
        enabled = true,
        tokenExpired = false
    )

    fun loginUser(email: String, password: String): Result<User> {
       return if (email == mockUser.email && password == mockUser.password) {
           Result.success(mockUser)
       } else {
           Result.failure(Exception("Invalid credentials"))
       }
    }
}