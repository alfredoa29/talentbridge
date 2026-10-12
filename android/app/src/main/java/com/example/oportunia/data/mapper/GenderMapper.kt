package com.example.oportunia.data.mapper

import com.example.oportunia.data.remote.dto.GenderDto
import com.example.oportunia.data.remote.dto.SkillDto
import com.example.oportunia.domain.model.Gender
import com.example.oportunia.domain.model.Skill
import javax.inject.Inject

class GenderMapper @Inject constructor()  {

    fun mapToDomain(genderDto: GenderDto): Gender {
        return Gender(
            id = genderDto.id,
            name = genderDto.name
        )
    }
    fun mapToDomainList(gendersDto: List<GenderDto>): List<Gender> {
        return gendersDto?.map { mapToDomain(it) } ?: emptyList() // Si skillsDto es null, retorna una lista vací
    }

    fun mapToDto(genderDto: Gender): GenderDto {
        return GenderDto(
            id = genderDto.id,
            name = genderDto.name
        )
    }
    fun mapToDtoList(genders: List<Gender>): List<GenderDto> {
        return genders.map { mapToDto(it) }
    }

}