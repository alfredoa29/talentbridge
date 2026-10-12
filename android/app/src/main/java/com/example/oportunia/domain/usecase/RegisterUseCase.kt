package com.example.oportunia.domain.usecase

import com.example.oportunia.data.mapper.UserMapper
import com.example.oportunia.data.remote.dto.CompanyInput
import com.example.oportunia.data.remote.dto.StudentInput
import com.example.oportunia.data.remote.dto.UserDto
import com.example.oportunia.domain.repository.AuthRepository
import com.example.oportunia.domain.repository.CompanyRepository
import com.example.oportunia.domain.repository.StudentRepository
import javax.inject.Inject


class RegisterUseCase @Inject constructor(
    private val studentRepository: StudentRepository,
    private val companyRepository: CompanyRepository,
    private val authRepository: AuthRepository
) {
    suspend fun registerAndLogin(email: String, password: String, role: String): Result<UserDto> {
        val registerResult = when (role) {
            "Estudiante" -> studentRepository.register(StudentInput(email, password))
            "Empresa" -> companyRepository.register(CompanyInput(email, password))
            else -> return Result.failure(Exception("Invalid role"))
        }

        return registerResult.fold(
            onSuccess = { userDto ->
                val loginResult = authRepository.login(email, password)
                loginResult.fold(
                    onSuccess = { Result.success(UserMapper().mapToDto(userDto)) },
                    onFailure = { Result.failure(Exception("Login failed after registration: ${it.message}")) }
                )
            },
            onFailure = { Result.failure(it) }
        )
    }
}