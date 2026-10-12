package com.example.oportunia.data.mapper

import com.example.oportunia.data.remote.dto.CountryDto
import com.example.oportunia.data.remote.dto.SkillDto
import com.example.oportunia.domain.model.Country
import com.example.oportunia.domain.model.Skill
import javax.inject.Inject

class CountryMapper @Inject constructor(){

    fun mapToDomain(countryDto: CountryDto): Country {
        return Country(
            id = countryDto.id,
            name = countryDto.name
        )
    }
    fun mapToDomainList(countryDto: List<CountryDto>): List<Country> {
        return countryDto?.map { mapToDomain(it) } ?: emptyList() // Si countryDto es null, retorna una lista vacía
    }

    fun mapToDto(country: Country): CountryDto {
        return CountryDto(
            id = country.id,
            name = country.name
        )
    }
    fun mapToDtoList(country: List<Country>): List<CountryDto> {
        return country.map { mapToDto(it) }
    }

}