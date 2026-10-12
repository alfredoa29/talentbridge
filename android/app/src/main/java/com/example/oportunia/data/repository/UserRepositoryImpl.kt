package com.example.oportunia.data.repository

import com.example.oportunia.data.mapper.UserMapper
import com.example.oportunia.data.remote.UserRemoteDataSource
import com.example.oportunia.data.remote.dto.UserDto
import com.example.oportunia.domain.model.User
import com.example.oportunia.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val userMapper: UserMapper
) : UserRepository {

    override suspend fun loginUser(email: String, password: String): Result<UserDto> {
        return try {
            val userToCheck = findUserByEmail(email)
            if(userToCheck.isSuccess) {
                val user = userToCheck.getOrThrow()

                return if(user.password == password){
                    Result.success(userMapper.mapToDto(user)) // make User a UserDTO for security reasons regarding the password
                }
                else{
                    Result.failure(Exception("Contraseña incorrecta!"))
                }
            }
            else {
                Result.failure(userToCheck.exceptionOrNull() ?: Exception("Usuario no encontrado"))
            }
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun findUserByEmail( email: String): Result<User> {
        return try {
            val usersResult = remoteDataSource.getUsers()
            if (usersResult.isSuccess) {
                usersResult.getOrThrow()
                    .firstOrNull { it.email == email }
                    ?.let { Result.success(it) }
                    ?: Result.failure(Exception("Usuario no encontrado"))
            } else {
                Result.failure(usersResult.exceptionOrNull() ?: Exception("Error"))
            }
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun findUserByUsername(username: String): Result<User>  =

        remoteDataSource.getUserByUsername(username).map {  userDto ->
            userMapper.mapToDomain(userDto)
        }



    override suspend fun create(email: String, password: String): Result<UserDto> {
        TODO("Not yet implemented")
    }
}