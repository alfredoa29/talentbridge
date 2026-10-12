package com.example.oportunia.data.mapper

import com.example.oportunia.data.remote.dto.UserDto
import com.example.oportunia.domain.model.User
import javax.inject.Inject

class UserMapper @Inject constructor(){
    fun mapToDto(user: User): UserDto {
        return UserDto(
            id = user.id,
            email = user.email,
            enabled = user.enabled,

            tokenExpired = user.tokenExpired
        )
    }

    fun mapToDomain(userDto: UserDto): User {
        return User(
            id = userDto.id,
            email = userDto.email,
            password = "null",
            enabled = userDto.enabled,

            tokenExpired = userDto.tokenExpired
        )
    }

    fun mapToDomainList(userDtos: List<UserDto>): List<User> {
        return userDtos.map { mapToDomain(it) }
    }

}